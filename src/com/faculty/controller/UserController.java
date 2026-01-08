package com.faculty.controller;

import com.faculty.dao.UserDAO;
import com.faculty.dao.UserDAOImpl;
import com.faculty.model.Role;
import com.faculty.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserController {
    private final UserDAO dao = new UserDAOImpl();

    public List<User> listAll() throws SQLException { return dao.findAll(); }
    public List<User> listByRole(Role role) throws SQLException { return dao.findByRole(role); }
    public Optional<User> findById(int id) throws SQLException { return dao.findById(id); }
    public int create(User u) throws SQLException { return dao.create(u); }
    public boolean update(User u) throws SQLException { return dao.update(u); }
    public boolean delete(int id) throws SQLException { return dao.delete(id); }
}
