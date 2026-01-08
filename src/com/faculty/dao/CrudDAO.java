package com.faculty.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, K> {
    K create(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(K id) throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
}
