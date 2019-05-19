package ru.mail.krivonos.al.repository.impl;

import ru.mail.krivonos.al.repository.GenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
    public List<T> findAll() {
        String query = String.format("from %s %s", entityClass.getName(), " c");
        Query q = entityManager.createQuery(query);
        return q.getResultList();
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

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAllWithDescendingOrder(int limit, int offset, String orderField) {
        String query = String.format("from %s order by %s desc", entityClass.getName(), orderField);
        Query q = entityManager.createQuery(query)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAllWithAscendingOrder(int limit, int offset, String orderField) {
        String query = String.format("from %s order by %s asc", entityClass.getName(), orderField);
        Query q = entityManager.createQuery(query)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return q.getResultList();
    }

    @Override
    public int getCountOfEntities() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(entityClass)));
        return Math.toIntExact(entityManager.createQuery(criteriaQuery).getSingleResult());
    }
}
