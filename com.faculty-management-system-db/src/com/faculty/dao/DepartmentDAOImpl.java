package com.faculty.dao;

import com.faculty.model.Department;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDAOImpl implements DepartmentDAO {

    @Override
    public Integer create(Department entity) throws SQLException {
        String sql = "INSERT INTO departments(name) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entity.setDeptId(id);
                    return id;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean update(Department entity) throws SQLException {
        String sql = "UPDATE departments SET name=? WHERE dept_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getDeptId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM departments WHERE dept_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<Department> findById(Integer id) throws SQLException {
        String sql = "SELECT d.dept_id, d.name, COUNT(l.lecturer_id) AS staff_count " +
                "FROM departments d LEFT JOIN lecturers l ON l.dept_id=d.dept_id " +
                "WHERE d.dept_id=? GROUP BY d.dept_id, d.name";
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
    public List<Department> findAll() throws SQLException {
        String sql = "SELECT d.dept_id, d.name, COUNT(l.lecturer_id) AS staff_count " +
                "FROM departments d LEFT JOIN lecturers l ON l.dept_id=d.dept_id " +
                "GROUP BY d.dept_id, d.name ORDER BY d.dept_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Department> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private Department map(ResultSet rs) throws SQLException {
        Department d = new Department(rs.getInt("dept_id"), rs.getString("name"));
        d.setStaffCount(rs.getInt("staff_count"));
        return d;
    }
}
