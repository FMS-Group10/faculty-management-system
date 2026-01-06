package com.faculty.controller;

import com.faculty.dao.LecturerDAO;
import com.faculty.dao.LecturerDAOImpl;
import com.faculty.model.Lecturer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LecturerController {
    private final LecturerDAO dao = new LecturerDAOImpl();

    public List<Lecturer> listAll() throws SQLException { return dao.findAll(); }
    public Optional<Lecturer> findById(int id) throws SQLException { return dao.findById(id); }
    public Optional<Lecturer> findByUserId(int userId) throws SQLException { return dao.findByUserId(userId); }
    public int create(Lecturer l) throws SQLException { return dao.create(l); }
    public boolean update(Lecturer l) throws SQLException { return dao.update(l); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
