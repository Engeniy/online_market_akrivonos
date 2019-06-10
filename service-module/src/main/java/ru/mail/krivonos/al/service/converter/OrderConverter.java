package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.Order;
import ru.mail.krivonos.al.service.model.OrderDTO;

public interface OrderConverter {

    OrderDTO toDTO(Order order);

    Order toEntity(OrderDTO orderDTO);
}
