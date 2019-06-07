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

import static ru.mail.krivonos.al.controller.constant.AttributeConstants.ITEM_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.ORDER_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.PAGE_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.CUSTOMER_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ORDERS_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ORDER_PAGE;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.ITEM_NUMBER_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.ORDER_ID_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.ORDER_NUMBER_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.PAGE_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDERS_ADD_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDERS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDER_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDER_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.INVALID_QUANTITY_PARAM;
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
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, Model model
    ) {
        PageDTO<OrderDTO> page = getPage(userPrincipal, pageNumber);
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return ORDERS_PAGE;
    }

    @GetMapping(ORDER_PAGE_URL)
    public String getOrder(
            @RequestParam(name = ORDER_NUMBER_PARAMETER) Long orderNumber, Model model
    ) {
        OrderDTO orderDTO = orderService.getOrderByOrderNumber(orderNumber);
        model.addAttribute(ORDER_ATTRIBUTE, orderDTO);
        return ORDER_PAGE;
    }

    @PostMapping(ORDER_UPDATE_URL)
    public String updateOrder(
            @RequestParam(ORDER_ID_PARAMETER) Long orderID,
            @ModelAttribute(ORDER_ATTRIBUTE) OrderDTO orderDTO
    ) {
        orderDTO.setId(orderID);
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderDTO);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ORDER_PAGE_URL, ORDER_NUMBER_PARAM,
                updatedOrder.getOrderNumber(), UPDATED_POSITIVE_PARAM);
    }

    @PostMapping(ORDERS_ADD_URL)
    public String saveOrder(
            @ModelAttribute(ORDER_ATTRIBUTE) @Valid OrderDTO orderDTO, BindingResult bindingResult, Model model,
            @RequestParam(ITEM_NUMBER_PARAMETER) Long itemID,
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal
    ) {
        if (bindingResult.hasErrors()) {
            ItemDTO item = itemService.getItemById(itemID);
            model.addAttribute(ITEM_ATTRIBUTE, item);
            return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ITEMS_PAGE_URL, PAGE_NUMBER_PARAM,
                    pageNumber, INVALID_QUANTITY_PARAM);
        }
        fillOrderAndAdd(itemID, userPrincipal.getUserID(), orderDTO);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ITEMS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, ORDER_CREATED_PARAM);
    }

    private PageDTO<OrderDTO> getPage(AuthUserPrincipal userPrincipal, Integer pageNumber) {
        for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
            String authorityAuthority = authority.getAuthority();
            if (SALE_AUTHORITY_NAME.equals(authorityAuthority)) {
                return orderService.getOrders(pageNumber);
            } else if (CUSTOMER_AUTHORITY_NAME.equals(authorityAuthority)) {
                return orderService.getOrdersByUserID(userPrincipal.getUserID(), pageNumber);
            }
        }
        return new PageDTO<>();
    }

    private void fillOrderAndAdd(Long itemID, Long userID, OrderDTO orderDTO) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(itemID);
        orderDTO.setItem(itemDTO);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userID);
        orderDTO.setUser(userDTO);
        orderService.add(orderDTO);
    }
}
