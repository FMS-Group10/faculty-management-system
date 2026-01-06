package com.faculty.controller;

import com.faculty.dao.DegreeDAO;
import com.faculty.dao.DegreeDAOImpl;
import com.faculty.model.Degree;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DegreeController {
    private final DegreeDAO dao = new DegreeDAOImpl();

    public List<Degree> listAll() throws SQLException { return dao.findAll(); }
    public Optional<Degree> findById(int id) throws SQLException { return dao.findById(id); }
    public int create(Degree d) throws SQLException { return dao.create(d); }
    public boolean update(Degree d) throws SQLException { return dao.update(d); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
