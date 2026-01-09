package com.faculty.dao;

import com.faculty.model.Enrollment;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAOImpl implements EnrollmentDAO {

    @Override
    public Integer create(Enrollment entity) throws SQLException {
        String sql = "INSERT INTO enrollments(student_id, course_code, grade) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getStudentId());
            ps.setInt(2, entity.getCourseCode());
            ps.setString(3, entity.getGrade());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entity.setEnrollmentId(id);
                    return id;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean update(Enrollment entity) throws SQLException {
        String sql = "UPDATE enrollments SET student_id=?, course_code=?, grade=? WHERE enrollment_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, entity.getStudentId());
            ps.setInt(2, entity.getCourseCode());
            ps.setString(3, entity.getGrade());
            ps.setInt(4, entity.getEnrollmentId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE enrollment_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<Enrollment> findById(Integer id) throws SQLException {
        String sql = baseSelect() + " WHERE e.enrollment_id=?";
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
    public List<Enrollment> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY e.enrollment_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Enrollment> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    @Override
    public List<Enrollment> findByStudentId(int studentId) throws SQLException {
        String sql = baseSelect() + " WHERE e.student_id=? ORDER BY e.enrollment_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Enrollment> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    private String baseSelect() {
        return "SELECT e.enrollment_id, e.student_id, e.course_code, e.grade, " +
                "CONCAT(s.first_name, ' ', s.last_name) AS student_name, c.course_name " +
                "FROM enrollments e " +
                "JOIN students s ON s.student_id=e.student_id " +
                "JOIN courses c ON c.course_code=e.course_code";
    }

    private Enrollment map(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getInt("student_id"),
                rs.getInt("course_code"),
                rs.getString("grade")
        );
        e.setStudentName(rs.getString("student_name"));
        e.setCourseName(rs.getString("course_name"));
        return e;
    }
}
