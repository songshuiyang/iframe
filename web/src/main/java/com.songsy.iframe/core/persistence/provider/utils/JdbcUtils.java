package com.songsy.iframe.core.persistence.provider.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {
    public final static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    public static void close(ResultSet rs, Statement stmt) {
        close(rs);
        close(stmt);
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC Statement", ex);
            } catch (Throwable ex) {
                logger.trace("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC ResultSet", ex);
            } catch (Throwable ex) {
                logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
            }
        }
    }

    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                logger.trace("Could not close JDBC Connection", ex);
            } catch (Throwable ex) {
                logger.trace("Unexpected exception on closing JDBC Connection", ex);
            }
        }
    }
}
