package com.faculty.dao;

import com.faculty.model.Lecturer;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LecturerDAOImpl implements LecturerDAO {

    @Override
    public Integer create(Lecturer entity) throws SQLException {
        String sql = "INSERT INTO lecturers(user_id, first_name, last_name, dept_id, email, mobile) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getUserId());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setInt(4, entity.getDeptId());
            ps.setString(5, entity.getEmail());
            ps.setString(6, entity.getMobile());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entity.setLecturerId(id);
                    return id;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean update(Lecturer entity) throws SQLException {
        String sql = "UPDATE lecturers SET first_name=?, last_name=?, dept_id=?, email=?, mobile=? WHERE lecturer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setInt(3, entity.getDeptId());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getMobile());
            ps.setInt(6, entity.getLecturerId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM lecturers WHERE lecturer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<Lecturer> findById(Integer id) throws SQLException {
        String sql = baseSelect() + " WHERE l.lecturer_id=?";
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
    public Optional<Lecturer> findByUserId(int userId) throws SQLException {
        String sql = baseSelect() + " WHERE l.user_id=?";
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
    public List<Lecturer> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY l.lecturer_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Lecturer> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private String baseSelect() {
        return "SELECT l.lecturer_id, l.user_id, u.username, l.first_name, l.last_name, l.dept_id, d.name AS dept_name, l.email, l.mobile " +
                "FROM lecturers l JOIN users u ON u.user_id=l.user_id JOIN departments d ON d.dept_id=l.dept_id";
    }

    private Lecturer map(ResultSet rs) throws SQLException {
        Lecturer l = new Lecturer(
                rs.getInt("lecturer_id"),
                rs.getInt("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("dept_id"),
                rs.getString("email"),
                rs.getString("mobile")
        );
        l.setUsername(rs.getString("username"));
        l.setDeptName(rs.getString("dept_name"));
        return l;
    }
}
