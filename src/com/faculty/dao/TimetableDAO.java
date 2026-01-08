package com.faculty.dao;

import com.faculty.model.TimetableEntry;

import java.sql.SQLException;
import java.util.List;

public interface TimetableDAO extends CrudDAO<TimetableEntry, Integer> {
    List<TimetableEntry> findByDegreeId(int degreeId) throws SQLException;
    List<TimetableEntry> findByLecturerId(int lecturerId) throws SQLException;
}
