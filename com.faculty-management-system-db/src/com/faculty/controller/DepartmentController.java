package com.faculty.controller;

import com.faculty.dao.DepartmentDAO;
import com.faculty.dao.DepartmentDAOImpl;
import com.faculty.model.Department;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DepartmentController {
    private final DepartmentDAO dao = new DepartmentDAOImpl();

    public List<Department> listAll() throws SQLException { return dao.findAll(); }
    public Optional<Department> findById(int id) throws SQLException { return dao.findById(id); }
    public int create(Department d) throws SQLException { return dao.create(d); }
    public boolean update(Department d) throws SQLException { return dao.update(d); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
