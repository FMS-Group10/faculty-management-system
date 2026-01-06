package com.faculty.dao;

import com.faculty.model.Student;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public Integer create(Student entity) throws SQLException {
        String sql = "INSERT INTO students(user_id, first_name, last_name, email, mobile, degree_id) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getUserId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getMobile());
            ps.setInt(6, entity.getDegreeId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entity.setStudentId(id);
                    return id;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean update(Student entity) throws SQLException {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, mobile=?, degree_id=? WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getMobile());
            ps.setInt(5, entity.getDegreeId());
            ps.setInt(6, entity.getStudentId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<Student> findById(Integer id) throws SQLException {
        String sql = baseSelect() + " WHERE s.student_id=?";
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
    public Optional<Student> findByUserId(int userId) throws SQLException {
        String sql = baseSelect() + " WHERE s.user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY s.student_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Student> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private String baseSelect() {
        return "SELECT s.student_id, s.first_name, s.last_name, s.user_id, u.username, s.email, s.mobile, s.degree_id, g.degree_name " +
                "FROM students s JOIN users u ON u.user_id=s.user_id JOIN degrees g ON g.degree_id=s.degree_id";
    }

    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student(
                rs.getInt("student_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("mobile"),
                rs.getInt("degree_id")
        );
        s.setUsername(rs.getString("username"));
        s.setDegreeName(rs.getString("degree_name"));
        return s;
    }
}
