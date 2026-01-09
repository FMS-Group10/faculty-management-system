package com.faculty.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        // Driver is auto-loaded in modern JDBC (when mysql-connector-j is on classpath).
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }

    public static void closeQuietly(AutoCloseable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Exception ignored) {
        }
    }
}
