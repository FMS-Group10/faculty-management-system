package com.faculty.dao;

import com.faculty.model.Role;
import com.faculty.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO extends CrudDAO<User, Integer> {
    Optional<User> findByUsername(String username) throws SQLException;
    Optional<User> authenticate(String username, String password) throws SQLException;
    List<User> findByRole(Role role) throws SQLException;
}
