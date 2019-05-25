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

import java.util.Date;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ArticleAPIControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetArticles() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        ArticleDTO[] admins = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/articles", ArticleDTO[].class);
        Assert.assertNotNull(admins);
    }

    @Test
    public void shouldGetArticle() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        Long id = 1L;
        ArticleDTO articleDTO = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/articles/1", ArticleDTO.class);
        Assert.assertEquals(id, articleDTO.getId());
    }

    @Test
    public void shouldSaveArticle() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setContent("Hello");
        articleDTO.setTitle("Title");
        articleDTO.setDateOfCreation(new Date());
        restTemplate.withBasicAuth("api@api.com", "admin");
        ArticleDTO admins = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .postForObject("http://localhost:8080/api/v1/articles", articleDTO, ArticleDTO.class);
        Assert.assertNotNull(admins.getId());
    }

    @Test
    public void shouldDeleteArticle() {
        restTemplate.withBasicAuth("api@api.com", "admin");
        restTemplate
                .withBasicAuth("api@api.com", "admin")
                .delete("http://localhost:8080/api/v1/articles/1", ResponseEntity.class);
        ArticleDTO articleDTO = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .getForObject("http://localhost:8080/api/v1/articles/1", ArticleDTO.class);
        Assert.assertNull(articleDTO);
    }
}
