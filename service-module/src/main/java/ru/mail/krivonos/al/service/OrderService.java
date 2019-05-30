package ru.mail.krivonos.al.service;

import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.service.model.OrderDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

public interface OrderService {

    PageDTO<OrderDTO> getOrders(Integer pageNumber);


    PageDTO<OrderDTO> getOrdersByUserID(Long userID, Integer pageNumber);

    OrderDTO getOrderByOrderNumber(Long orderNumber);

    OrderDTO updateOrderStatus(OrderDTO orderDTO);

    @Transactional
    void add(OrderDTO orderDTO);
}
