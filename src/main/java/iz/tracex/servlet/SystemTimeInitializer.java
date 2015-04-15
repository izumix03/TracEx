package iz.tracex.servlet;

import iz.tracex.base.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public final class SystemTimeInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SystemTimeInitializer.class);

    private SystemTimeInitializer() {
    }

    public static void initialize() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConnectionManager.getConnection();
            ps = conn.prepareStatement("SELECT NOW()");
            rs = ps.executeQuery();
            rs.next();

            final java.sql.Timestamp databaseNow = rs.getTimestamp(1);
            logger.info("Database now = {}", databaseNow);

            DateTimeUtils.setCurrentMillisOffset(databaseNow.getTime() - System.currentTimeMillis());

        } catch (SQLException e) {
            throw new IllegalStateException("Failed to 'SELECT NOW()' !!!!", e);
        } finally {
            DbUtils.closeQuietly(conn, ps, rs);
        }
    }
}
