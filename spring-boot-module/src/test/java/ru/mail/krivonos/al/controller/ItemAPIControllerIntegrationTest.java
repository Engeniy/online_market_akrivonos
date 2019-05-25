package ru.mail.krivonos.al.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.ItemDTO;

import java.util.Date;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ItemAPIControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetItems() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        ItemDTO[] items = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/items", ItemDTO[].class);
        Assert.assertNotNull(items);
    }

    @Test
    public void shouldGetItem() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        Long id = 1L;
        ItemDTO itemDTO = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/items/1", ItemDTO.class);
        Assert.assertEquals(id, itemDTO.getId());
    }

    @Test
    public void shouldSaveItem() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Name");
        itemDTO.setUniqueNumber("123456");
        itemDTO.setPrice("1.23");
        itemDTO.setDescription("Description");
        restTemplate.withBasicAuth("api@api.com", "admin");
        ItemDTO admins = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .postForObject("http://localhost:8080/api/v1/items", itemDTO, ItemDTO.class);
        Assert.assertNotNull(admins.getId());
    }

    @Test
    public void shouldDeleteItem() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        restTemplate
                .withBasicAuth("api@api.com", "admin")
                .delete("http://localhost:8080/api/v1/items/1", ResponseEntity.class);
        ItemDTO itemDTO = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/items/1", ItemDTO.class);
        Assert.assertNull(itemDTO);
    }
}
