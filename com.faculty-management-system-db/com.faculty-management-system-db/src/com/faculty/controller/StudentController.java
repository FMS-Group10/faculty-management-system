package com.faculty.controller;

import com.faculty.dao.StudentDAO;
import com.faculty.dao.StudentDAOImpl;
import com.faculty.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentController {
    private final StudentDAO dao = new StudentDAOImpl();

    public List<Student> listAll() throws SQLException { return dao.findAll(); }
    public Optional<Student> findById(int id) throws SQLException { return dao.findById(id); }
    public Optional<Student> findByUserId(int userId) throws SQLException { return dao.findByUserId(userId); }
    public int create(Student s) throws SQLException { return dao.create(s); }
    public boolean update(Student s) throws SQLException { return dao.update(s); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
