package com.faculty.dao;

import com.faculty.model.Enrollment;

import java.sql.SQLException;
import java.util.List;

public interface EnrollmentDAO extends CrudDAO<Enrollment, Integer> {
    List<Enrollment> findByStudentId(int studentId) throws SQLException;
}
