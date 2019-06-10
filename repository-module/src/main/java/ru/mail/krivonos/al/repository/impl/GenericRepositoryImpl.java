package ru.mail.krivonos.al.repository.impl;

import ru.mail.krivonos.al.repository.GenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
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
    public T findByIdNotDeleted(I id) {
        String queryString = String.format("from %s %s", entityClass.getName(),
                "where id = :id and deleted = 0");
        Query query = entityManager.createQuery(queryString);
        query.setParameter("id", id);
        try {
            return (T) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        String queryString = String.format("from %s", entityClass.getName());
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(int limit, int offset) {
        String queryString = String.format("from %s", entityClass.getName());
        Query query = entityManager.createQuery(queryString)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAllNotDeletedWithAscendingOrder(int limit, int offset, String orderField) {
        String queryString = String.format("from %s where deleted = 0 order by %s asc",
                entityClass.getName(), orderField);
        Query query = entityManager.createQuery(queryString)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAllWithDescendingOrder(int limit, int offset, String orderField) {
        String queryString = String.format("from %s order by %s desc", entityClass.getName(), orderField);
        Query query = entityManager.createQuery(queryString)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public int getCountOfEntities() {
        String queryString = String.format("select count(*) from %s", entityClass.getName());
        Query query = entityManager.createQuery(queryString);
        return ((Number) query.getSingleResult()).intValue();
    }

    @Override
    public int getCountOfNotDeletedEntities() {
        String queryString = String.format("select count(*) from %s where deleted = 0", entityClass.getName());
        Query query = entityManager.createQuery(queryString);
        return ((Number) query.getSingleResult()).intValue();
    }
}
