package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ItemRepository;
import ru.mail.krivonos.al.repository.OrderRepository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.Item;
import ru.mail.krivonos.al.repository.model.Order;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.repository.model.enums.OrderStatusEnum;
import ru.mail.krivonos.al.service.OrderService;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.converter.OrderConverter;
import ru.mail.krivonos.al.service.model.OrderDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.ORDERS_LIMIT;
import static ru.mail.krivonos.al.service.constant.OrderConstants.DATE_OF_CREATION;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final long ORDER_NUMBER_DIVIDER = 100000000;

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PageCountingService pageCountingService;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderConverter orderConverter,
            UserRepository userRepository,
            ItemRepository itemRepository,
            PageCountingService pageCountingService
    ) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.pageCountingService = pageCountingService;
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrders(int pageNumber) {
        int countOfEntities = orderRepository.getCountOfEntities();
        PageDTO<OrderDTO> pageDTO = new PageDTO<>();
        int offset = getOffsetAndSetPages(pageDTO, pageNumber, countOfEntities);
        List<Order> orders = orderRepository.findAllWithDescendingOrder(ORDERS_LIMIT, offset, DATE_OF_CREATION);
        List<OrderDTO> orderDTOs = getOrderDTOs(orders);
        pageDTO.setList(orderDTOs);
        return pageDTO;
    }

    @Override
    @Transactional
    public List<OrderDTO> getOrders(int limit, int offset) {
        List<Order> orders = orderRepository.findAll(limit, offset);
        return getOrderDTOs(orders);
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrdersByUserID(Long userID, int pageNumber) {
        User user = userRepository.findById(userID);
        int countOfEntities = orderRepository.getCountOfOrdersForUser(user);
        PageDTO<OrderDTO> pageDTO = new PageDTO<>();
        int offset = getOffsetAndSetPages(pageDTO, pageNumber, countOfEntities);
        List<Order> orders = orderRepository.findAllForUser(ORDERS_LIMIT, offset, user);
        List<OrderDTO> orderDTOs = getOrderDTOs(orders);
        pageDTO.setList(orderDTOs);
        return pageDTO;
    }

    @Override
    @Transactional
    public OrderDTO getOrderByOrderNumber(Long orderNumber) {
        Order order = orderRepository.findOrderByOrderNumber(orderNumber);
        return getOrderDTOWithTotalPrice(order);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId());
        order.setStatus(orderDTO.getStatus());
        orderRepository.merge(order);
        return orderConverter.toDTO(order);
    }

    @Override
    @Transactional
    public void add(OrderDTO orderDTO) {
        Order order = orderConverter.toEntity(orderDTO);
        Item item = itemRepository.findById(orderDTO.getItem().getId());
        order.setItem(item);
        User user = userRepository.findById(orderDTO.getUser().getId());
        order.setUser(user);
        order.setDateOfCreation(new Date());
        order.setOrderNumber(getOrderNumber());
        order.setStatus(OrderStatusEnum.NEW);
        orderRepository.persist(order);
    }

    @Override
    @Transactional
    public OrderDTO getOrderByID(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            return null;
        }
        return orderConverter.toDTO(order);
    }

    private List<OrderDTO> getOrderDTOs(List<Order> orders) {
        return orders.stream()
                .map(this::getOrderDTOWithTotalPrice)
                .collect(Collectors.toList());
    }

    private OrderDTO getOrderDTOWithTotalPrice(Order order) {
        OrderDTO orderDTO = orderConverter.toDTO(order);
        BigDecimal priceMultiplier = BigDecimal.valueOf(order.getQuantity());
        BigDecimal totalPrice = order.getItem().getPrice().multiply(priceMultiplier);
        orderDTO.setTotalPrice(totalPrice);
        return orderDTO;
    }

    private int getOffsetAndSetPages(PageDTO<OrderDTO> pageDTO, Integer pageNumber, int countOfEntities) {
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, ORDERS_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        return pageCountingService.getOffset(currentPageNumber, ORDERS_LIMIT);
    }

    private long getOrderNumber() {
        return System.currentTimeMillis() % ORDER_NUMBER_DIVIDER;
    }
}
