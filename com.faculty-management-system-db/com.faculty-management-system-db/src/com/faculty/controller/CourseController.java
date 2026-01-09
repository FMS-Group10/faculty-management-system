package com.faculty.controller;

import com.faculty.dao.CourseDAO;
import com.faculty.dao.CourseDAOImpl;
import com.faculty.model.Course;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseController {
    private final CourseDAO dao = new CourseDAOImpl();

    public List<Course> listAll() throws SQLException { return dao.findAll(); }
    public Optional<Course> findByCode(int courseCode) throws SQLException { return dao.findById(courseCode); }
    public List<Course> listByLecturerId(int lecturerId) throws SQLException { return dao.findByLecturerId(lecturerId); }
    public List<Course> listByDegreeId(int degreeId) throws SQLException { return dao.findByDegreeId(degreeId); }
    public int create(Course c) throws SQLException { return dao.create(c); }
    public boolean update(Course c) throws SQLException { return dao.update(c); }
    public boolean delete(int courseCode) throws SQLException { return dao.delete(courseCode); }
}
