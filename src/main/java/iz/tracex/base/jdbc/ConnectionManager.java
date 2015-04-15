package iz.tracex.base.jdbc;

import iz.tracex.base.TracExProperties;
import iz.tracex.base.TracExProperties.Name;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.tomcat.jdbc.pool.ConnectionPool;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.JdbcInterceptor;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.PooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public final class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private static final DataSource DS;
    static {
        try {
            final PoolProperties p = new PoolProperties();
            // 接続情報
            p.setUrl("jdbc:mysql://" + TracExProperties.getString(Name.SERVER) + ":3306/"
                    + TracExProperties.getString(Name.DATABASE));
            p.setUsername("root");
            p.setPassword("root");

            // 基本設定
            p.setDriverClassName("com.mysql.jdbc.Driver");
            p.setDefaultAutoCommit(false);
            p.setInitialSize(0);
            //サーバーにおくときは20、それ以外は3
            if (TracExProperties.getString(Name.USER).equals("admin")){
            	p.setMaxActive(20);
            }else{
            	p.setMaxActive(3);
            }            
            p.setMaxIdle(3);
            p.setMinIdle(0);

            // 検証設定
            p.setValidationQuery("SELECT 1 FROM DUAL");
            p.setTestOnBorrow(true);

            // アイドル状態に関する設定
            if (TracExProperties.getBoolean(Name.DEVELOP)){
            	p.setTimeBetweenEvictionRunsMillis(3 * 60 * 1000);// 3分
            	p.setMinEvictableIdleTimeMillis(15 * 60 * 1000);// 15分
            }
            
            // Interceptor
            p.setJdbcInterceptors(LoggingInterceptor.class.getName());

            DS = new DataSource(p);

        } catch (Exception e) {
            logger.error("Failed to create dataSource!", e);
            throw new IllegalStateException(e);
        }
    }

    public static class LoggingInterceptor extends JdbcInterceptor {
        @Override
        public void reset(ConnectionPool parent, PooledConnection con) {
            logger.trace("called #reset");
        }

        @Override
        public void disconnected(ConnectionPool parent, PooledConnection con, boolean finalizing) {
            logger.trace("called #disconnected");
        }
    }

    private static final ThreadLocal<Connection> HOLDER = new ThreadLocal<Connection>() {
        @Override
        protected Connection initialValue() {
            return null;
        }
    };

    /**
     * @return connection
     */
    public static Connection getConnection() {
        Connection conn = HOLDER.get();
        if (conn == null) {
            try {
                conn = DS.getConnection();
                HOLDER.set(conn);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
        return conn;
    }

    /**
     * Commit & close.
     */
    public static void commitAndClose() {
        Connection conn = HOLDER.get();
        if (conn != null) {
            logger.debug("Commit and close Connection.");
            DbUtils.commitAndCloseQuietly(conn);
        }
        HOLDER.remove();
    }

    /**
     * Rollback & close.
     */
    public static void rollbackAndClose() {
        Connection conn = HOLDER.get();
        if (conn != null) {
            logger.debug("Rollback and close Connection.");
            DbUtils.rollbackAndCloseQuietly(conn);
        }
        HOLDER.remove();
    }
}
