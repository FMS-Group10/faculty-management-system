package com.faculty.controller;

import com.faculty.dao.EnrollmentDAO;
import com.faculty.dao.EnrollmentDAOImpl;
import com.faculty.model.Enrollment;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EnrollmentController {
    private final EnrollmentDAO dao = new EnrollmentDAOImpl();

    public List<Enrollment> listAll() throws SQLException { return dao.findAll(); }
    public List<Enrollment> listByStudentId(int studentId) throws SQLException { return dao.findByStudentId(studentId); }
    public Optional<Enrollment> findById(int id) throws SQLException { return dao.findById(id); }
    public int create(Enrollment e) throws SQLException { return dao.create(e); }
    public boolean update(Enrollment e) throws SQLException { return dao.update(e); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
