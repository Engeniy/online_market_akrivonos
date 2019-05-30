package ru.mail.krivonos.al.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.krivonos.al.repository.model.OrderStatusEnum;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.OrderDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ADD_ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.DELETE_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.EDIT_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDERS_ADD_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDERS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDER_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ORDER_UPDATE_URL;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldReturnOrdersPageForGetRequest() throws Exception {
        mockMvc.perform(get(ORDERS_PAGE_URL)).andExpect(status().isOk());
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void requestForOrderPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get(ORDER_PAGE_URL + "?order_number=1111")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Cat collar")));
    }

    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldReturnRedirectToItemsPageWithPositiveParamForSuccessfulAddOrderRequest() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setQuantity(5);
        this.mockMvc.perform(post(ORDERS_ADD_URL + "?item_number=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", orderDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/items?page=1&order_created"));
    }

    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldReturnRedirectToItemsPageWithNegativeParamForUnsuccessfulAddOrderRequest() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setQuantity(-1);
        this.mockMvc.perform(post(ORDERS_ADD_URL + "?item_number=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", orderDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/items?page=1&invalid_quantity"));
    }
}
