package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.krivonos.al.service.OrderService;
import ru.mail.krivonos.al.service.model.OrderDTO;

import java.util.List;

import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_ORDERS_URL;
import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_ORDERS_WITH_ID_URL;
import static ru.mail.krivonos.al.controller.constant.PathVariableConstants.ID_VARIABLE;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.LIMIT_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.OFFSET_PARAMETER;

@RestController("orderApiController")
public class OrderApiController {

    private final OrderService orderService;

    @Autowired
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(API_ORDERS_URL)
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<OrderDTO>> getOrders(
            @RequestParam(name = LIMIT_PARAMETER, defaultValue = "10") Integer limit,
            @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") Integer offset
    ) {
        List<OrderDTO> orders = orderService.getOrders(limit, offset);
        return new ResponseEntity(orders, HttpStatus.OK);
    }

    @GetMapping(API_ORDERS_WITH_ID_URL)
    @SuppressWarnings("unchecked")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable(ID_VARIABLE) Long id
    ) {
        OrderDTO order = orderService.getOrderByID(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(order, HttpStatus.OK);
    }
}
