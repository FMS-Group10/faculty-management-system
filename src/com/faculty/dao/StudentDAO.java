package com.faculty.dao;

import com.faculty.model.Student;

import java.sql.SQLException;
import java.util.Optional;

public interface StudentDAO extends CrudDAO<Student, Integer> {
    Optional<Student> findByUserId(int userId) throws SQLException;
}
