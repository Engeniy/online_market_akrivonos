package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Order;
import ru.mail.krivonos.al.service.converter.ItemConverterAggregator;
import ru.mail.krivonos.al.service.converter.OrderConverter;
import ru.mail.krivonos.al.service.converter.UserConverterAggregator;
import ru.mail.krivonos.al.service.model.OrderDTO;

@Component("orderConverter")
public class OrderConverterImpl implements OrderConverter {

    private final UserConverterAggregator userConverterAggregator;
    private final ItemConverterAggregator itemConverterAggregator;

    public OrderConverterImpl(
            UserConverterAggregator userConverterAggregator,
            ItemConverterAggregator itemConverterAggregator
    ) {
        this.userConverterAggregator = userConverterAggregator;
        this.itemConverterAggregator = itemConverterAggregator;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setUser(userConverterAggregator.getOrderUserConverter().toDTO(order.getUser()));
        orderDTO.setItem(itemConverterAggregator.getOrderItemConverter().toDTO(order.getItem()));
        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setQuantity(orderDTO.getQuantity());
        order.setUser(userConverterAggregator.getOrderUserConverter().toEntity(orderDTO.getUser()));
        order.setItem(itemConverterAggregator.getOrderItemConverter().toEntity(orderDTO.getItem()));
        return order;
    }
}
