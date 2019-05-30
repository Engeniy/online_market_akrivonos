package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.Order;
import ru.mail.krivonos.al.repository.model.User;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> findAllForUser(int limit, int offset, User user);

    Order findOrderByOrderNumber(Long orderNumber);
}
