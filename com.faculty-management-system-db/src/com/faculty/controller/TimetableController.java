package com.faculty.controller;

import com.faculty.dao.TimetableDAO;
import com.faculty.dao.TimetableDAOImpl;
import com.faculty.model.TimetableEntry;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TimetableController {
    private final TimetableDAO dao = new TimetableDAOImpl();

    public List<TimetableEntry> listAll() throws SQLException { return dao.findAll(); }
    public List<TimetableEntry> listByDegreeId(int degreeId) throws SQLException { return dao.findByDegreeId(degreeId); }
    public List<TimetableEntry> listByLecturerId(int lecturerId) throws SQLException { return dao.findByLecturerId(lecturerId); }
    public Optional<TimetableEntry> findById(int id) throws SQLException { return dao.findById(id); }
    public int create(TimetableEntry t) throws SQLException { return dao.create(t); }
    public boolean update(TimetableEntry t) throws SQLException { return dao.update(t); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
