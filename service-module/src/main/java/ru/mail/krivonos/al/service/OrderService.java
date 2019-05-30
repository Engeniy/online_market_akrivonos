package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.OrderDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.List;

public interface OrderService {

    PageDTO<OrderDTO> getOrders(int pageNumber);

    List<OrderDTO> getOrders(int limit, int offset);

    PageDTO<OrderDTO> getOrdersByUserID(Long userID, Integer pageNumber);

    OrderDTO getOrderByOrderNumber(Long orderNumber);

    OrderDTO updateOrderStatus(OrderDTO orderDTO);

    void add(OrderDTO orderDTO);

    OrderDTO getOrderByID(Long id);
}
