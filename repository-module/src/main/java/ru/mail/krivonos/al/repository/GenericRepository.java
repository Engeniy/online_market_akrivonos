package ru.mail.krivonos.al.repository;

import java.sql.Connection;
import java.util.List;

public interface GenericRepository<I, T> {

    Connection getConnection();

    void persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T findById(I id);

    List<T> findAll(int limit, int offset);
}
