package iz.tracex.base.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author izumi_j
 *
 */
public final class GeneratedKeysReturnQueryRunner extends QueryRunner {

    @Override
    protected PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    }

    /**
     * @param conn
     * @param sql
     * @param params
     * @return new key
     * @throws SQLException
     */
    public long insert(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long key = 0;
        try {
            stmt = this.prepareStatement(conn, sql);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                key = rs.getLong(1);
            }
        } catch (SQLException e) {
            this.rethrow(e, sql, params);
        } finally {
            close(stmt);
        }
        return key;
    }
}
