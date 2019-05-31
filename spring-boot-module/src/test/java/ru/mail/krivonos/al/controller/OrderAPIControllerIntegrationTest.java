package ru.mail.krivonos.al.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.krivonos.al.service.model.OrderDTO;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderAPIControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetOrders() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        OrderDTO[] orders = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/orders", OrderDTO[].class);
        Assert.assertNotNull(orders);
    }

    @Test
    public void shouldGetOrder() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        Long id = 1L;
        OrderDTO orderDTO = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/orders/1", OrderDTO.class);
        Assert.assertEquals(id, orderDTO.getId());
    }
}
