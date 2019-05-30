package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.ItemService;
import ru.mail.krivonos.al.service.OrderService;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.ItemDTO;
import ru.mail.krivonos.al.service.model.OrderDTO;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import javax.validation.Valid;

import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.CUSTOMER_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ITEM_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ORDERS_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ORDER_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEM_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDERS_ADD_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDERS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDER_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDER_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.INVALID_QUANTITY_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ITEM_NUMBER_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ORDER_CREATED_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ORDER_NUMBER_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PAGE_NUMBER_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPDATED_POSITIVE_PARAM;

@Controller("orderController")
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    @Autowired
    public OrderController(
            OrderService orderService,
            ItemService itemService
    ) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @GetMapping(ORDERS_PAGE_URL)
    public String getOrders(
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, Model model
    ) {
        PageDTO<OrderDTO> page = getPage(userPrincipal, pageNumber);
        model.addAttribute("page", page);
        return ORDERS_PAGE;
    }

    @GetMapping(ORDER_PAGE_URL)
    public String getOrder(
            @RequestParam(name = "order_number") Long orderNumber, Model model
    ) {
        OrderDTO orderDTO = orderService.getOrderByOrderNumber(orderNumber);
        model.addAttribute("order", orderDTO);
        return ORDER_PAGE;
    }

    @PostMapping(ORDER_UPDATE_URL)
    public String updateOrder(
            @RequestParam("order_id") Long orderID,
            @ModelAttribute("order") OrderDTO orderDTO
    ) {
        orderDTO.setId(orderID);
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderDTO);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ORDER_PAGE_URL, ORDER_NUMBER_PARAM,
                updatedOrder.getOrderNumber(), UPDATED_POSITIVE_PARAM);
    }

    @PostMapping(ORDERS_ADD_URL)
    public String saveOrder(
            @ModelAttribute("order") @Valid OrderDTO orderDTO, BindingResult bindingResult, Model model,
            @RequestParam("item_number") Long itemID,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal
    ) {
        if (bindingResult.hasErrors()) {
            ItemDTO item = itemService.getItemById(itemID);
            model.addAttribute("item", item);
            return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ITEMS_PAGE_URL, PAGE_NUMBER_PARAM,
                    pageNumber, INVALID_QUANTITY_PARAM);
        }
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(itemID);
        orderDTO.setItem(itemDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userPrincipal.getUserID());
        orderDTO.setUser(userDTO);
        orderService.add(orderDTO);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ITEMS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, ORDER_CREATED_PARAM);
    }

    private PageDTO<OrderDTO> getPage(AuthUserPrincipal userPrincipal, Integer pageNumber) {
        for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
            switch (authority.getAuthority()) {
                case SALE_AUTHORITY_NAME:
                    return orderService.getOrders(pageNumber);
                case CUSTOMER_AUTHORITY_NAME:
                    return orderService.getOrdersByUserID(userPrincipal.getUserID(), pageNumber);
            }
        }
        return new PageDTO<>();
    }
}
