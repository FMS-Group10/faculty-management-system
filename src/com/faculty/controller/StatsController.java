package com.faculty.controller;

import com.faculty.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsController {
    public int countTable(String table) throws SQLException {
        // Basic whitelist to prevent injection
        switch (table) {
            case "users", "students", "lecturers", "courses", "departments", "degrees", "enrollments", "timetable" -> {}
            default -> throw new IllegalArgumentException("Invalid table name");
        }

        String sql = "SELECT COUNT(*) AS c FROM " + table;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt("c");
        }
    }
}
