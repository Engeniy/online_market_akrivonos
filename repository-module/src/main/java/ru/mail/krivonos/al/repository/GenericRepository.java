package ru.mail.krivonos.al.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    void persist(T entity);

    void merge(T entity);

    void remove(T entity);

    T findById(I id);

    List<T> findAll();

    List<T> findAll(int limit, int offset);

    List<T> findAllWithDescendingOrder(int limit, int offset, String orderField);

    List<T> findAllWithAscendingOrder(int limit, int offset, String orderField);

    int getCountOfEntities();
}
