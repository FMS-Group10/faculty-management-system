package com.faculty.dao;

import com.faculty.model.Course;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public Integer create(Course entity) throws SQLException {
        String sql = "INSERT INTO courses(course_code, course_name, credits, lecturer_id, degree_id) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getCourseCode());
            ps.setString(2, entity.getCourseName());
            ps.setInt(3, entity.getCredits());
            if (entity.getLecturerId() == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, entity.getLecturerId());
            ps.setInt(5, entity.getDegreeId());
            ps.executeUpdate();
            return entity.getCourseCode();
        }
    }

    @Override
    public boolean update(Course entity) throws SQLException {
        String sql = "UPDATE courses SET course_name=?, credits=?, lecturer_id=?, degree_id=? WHERE course_code=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getCourseName());
            ps.setInt(2, entity.getCredits());
            if (entity.getLecturerId() == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, entity.getLecturerId());
            ps.setInt(4, entity.getDegreeId());
            ps.setInt(5, entity.getCourseCode());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_code=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<Course> findById(Integer id) throws SQLException {
        String sql = baseSelect() + " WHERE c.course_code=?";
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
    public List<Course> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY c.course_code DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Course> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public List<Course> findByLecturerId(int lecturerId) throws SQLException {
        String sql = baseSelect() + " WHERE c.lecturer_id=? ORDER BY c.course_code DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lecturerId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Course> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    @Override
    public List<Course> findByDegreeId(int degreeId) throws SQLException {
        String sql = baseSelect() + " WHERE c.degree_id=? ORDER BY c.course_code DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, degreeId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Course> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private String baseSelect() {
        return "SELECT c.course_code, c.course_name, c.credits, c.lecturer_id, c.degree_id, " +
                "CONCAT(l.first_name, ' ', l.last_name) AS lecturer_name, g.degree_name " +
                "FROM courses c " +
                "LEFT JOIN lecturers l ON l.lecturer_id=c.lecturer_id " +
                "JOIN degrees g ON g.degree_id=c.degree_id";
    }

    private Course map(ResultSet rs) throws SQLException {
        Integer lecId = rs.getObject("lecturer_id") == null ? null : rs.getInt("lecturer_id");
        Course c = new Course(
                rs.getInt("course_code"),
                rs.getString("course_name"),
                rs.getInt("credits"),
                lecId,
                rs.getInt("degree_id")
        );
        c.setLecturerName(rs.getString("lecturer_name"));
        c.setDegreeName(rs.getString("degree_name"));
        return c;
    }
}
