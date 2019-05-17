package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mail.krivonos.al.repository.GenericRepository;
import ru.mail.krivonos.al.repository.exceptions.DatabaseConnectionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    private static final String DATABASE_CONNECTION_ERROR_MESSAGE = "Can't create connection to database.";

    private static final Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseConnectionException(DATABASE_CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public T findById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(int limit, int offset) {
        String query = String.format("from %s %s", entityClass.getName(), " c");
        Query q = entityManager.createQuery(query)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return q.getResultList();
    }
}
