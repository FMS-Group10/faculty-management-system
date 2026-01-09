package com.faculty.dao;

import com.faculty.model.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDAO extends CrudDAO<Course, Integer> {
    List<Course> findByLecturerId(int lecturerId) throws SQLException;
    List<Course> findByDegreeId(int degreeId) throws SQLException;
}
