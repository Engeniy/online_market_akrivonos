package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.OrderRepository;
import ru.mail.krivonos.al.repository.model.Order;
import ru.mail.krivonos.al.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository("orderRepository")
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findAllForUser(int limit, int offset, User user) {
        String queryString = String.format("from %s where user = :user order by date_of_creation desc",
                entityClass.getName());
        Query query = entityManager.createQuery(queryString)
                .setParameter("user", user)
                .setMaxResults(limit)
                .setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public Order findOrderByOrderNumber(Long orderNumber) {
        String queryString = String.format("from %s where order_number = :order_number order by date_of_creation desc",
                entityClass.getName());
        Query query = entityManager.createQuery(queryString)
                .setParameter("order_number", orderNumber);
        try {
            return (Order) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
