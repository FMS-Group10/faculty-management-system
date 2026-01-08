package com.faculty.dao;

import com.faculty.model.TimetableEntry;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TimetableDAOImpl implements TimetableDAO {

    @Override
    public Integer create(TimetableEntry entity) throws SQLException {
        String sql = "INSERT INTO timetable(course_code, day_of_week, start_time, end_time) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getCourseCode());
            ps.setString(2, entity.getDayOfWeek());
            ps.setTime(3, Time.valueOf(entity.getStartTime()));
            ps.setTime(4, Time.valueOf(entity.getEndTime()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entity.setId(id);
                    return id;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean update(TimetableEntry entity) throws SQLException {
        String sql = "UPDATE timetable SET course_code=?, day_of_week=?, start_time=?, end_time=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getCourseCode());
            ps.setString(2, entity.getDayOfWeek());
            ps.setTime(3, Time.valueOf(entity.getStartTime()));
            ps.setTime(4, Time.valueOf(entity.getEndTime()));
            ps.setInt(5, entity.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM timetable WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<TimetableEntry> findById(Integer id) throws SQLException {
        String sql = baseSelect() + " WHERE t.id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<TimetableEntry> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<TimetableEntry> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public List<TimetableEntry> findByDegreeId(int degreeId) throws SQLException {
        String sql = baseSelect() + " WHERE c.degree_id=? ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, degreeId);
            try (ResultSet rs = ps.executeQuery()) {
                List<TimetableEntry> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    @Override
    public List<TimetableEntry> findByLecturerId(int lecturerId) throws SQLException {
        String sql = baseSelect() + " WHERE c.lecturer_id=? ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                List<TimetableEntry> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private String baseSelect() {
        return "SELECT t.id, t.course_code, t.day_of_week, t.start_time, t.end_time, c.course_name " +
                "FROM timetable t JOIN courses c ON c.course_code=t.course_code";
    }

    private TimetableEntry map(ResultSet rs) throws SQLException {
        Time start = rs.getTime("start_time");
        Time end = rs.getTime("end_time");
        TimetableEntry t = new TimetableEntry(
                rs.getInt("id"),
                rs.getInt("course_code"),
                rs.getString("day_of_week"),
                start == null ? null : start.toLocalTime(),
                end == null ? null : end.toLocalTime()
        );
        t.setCourseName(rs.getString("course_name"));
        return t;
    }
}
