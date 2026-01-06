package com.faculty.dao;

import com.faculty.model.Degree;
import com.faculty.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DegreeDAOImpl implements DegreeDAO {

    @Override
    public Integer create(Degree entity) throws SQLException {
        String sql = "INSERT INTO degrees(degree_name, dept_id, duration_years) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getDegreeName());
            ps.setInt(2, entity.getDeptId());
            ps.setInt(3, entity.getDurationYears());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entity.setDegreeId(id);
                    return id;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean update(Degree entity) throws SQLException {
        String sql = "UPDATE degrees SET degree_name=?, dept_id=?, duration_years=? WHERE degree_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entity.getDegreeName());
            ps.setInt(2, entity.getDeptId());
            ps.setInt(3, entity.getDurationYears());
            ps.setInt(4, entity.getDegreeId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM degrees WHERE degree_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public Optional<Degree> findById(Integer id) throws SQLException {
        String sql = "SELECT g.degree_id, g.degree_name, g.dept_id, g.duration_years, d.name AS dept_name " +
                "FROM degrees g JOIN departments d ON d.dept_id=g.dept_id WHERE g.degree_id=?";
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
    public List<Degree> findAll() throws SQLException {
        String sql = "SELECT g.degree_id, g.degree_name, g.dept_id, g.duration_years, d.name AS dept_name " +
                "FROM degrees g JOIN departments d ON d.dept_id=g.dept_id ORDER BY g.degree_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Degree> out = new ArrayList<>();
            while (rs.next()) out.add(map(rs));
            return out;
        }
    }

    private Degree map(ResultSet rs) throws SQLException {
        Degree g = new Degree(
                rs.getInt("degree_id"),
                rs.getString("degree_name"),
                rs.getInt("dept_id"),
                rs.getInt("duration_years")
        );
        g.setDeptName(rs.getString("dept_name"));
        return g;
    }
}
