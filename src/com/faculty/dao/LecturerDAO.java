package com.faculty.dao;

import com.faculty.model.Lecturer;

import java.sql.SQLException;
import java.util.Optional;

public interface LecturerDAO extends CrudDAO<Lecturer, Integer> {
    Optional<Lecturer> findByUserId(int userId) throws SQLException;
}
