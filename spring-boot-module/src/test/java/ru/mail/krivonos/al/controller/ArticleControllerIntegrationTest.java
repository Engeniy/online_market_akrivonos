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
import ru.mail.krivonos.al.service.model.ArticleDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.CUSTOMER_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ADD_ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.DELETE_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.EDIT_ARTICLE_URL;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldReturnArticlesPageForGetRequest() throws Exception {
        mockMvc.perform(get(ARTICLES_PAGE_URL)).andExpect(status().isOk());
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void requestForArticlesPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get(ARTICLES_PAGE_URL)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Some summary")));
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldReturnArticlePageForGetRequest() throws Exception {
        mockMvc.perform(get(ARTICLE_PAGE_URL + "?article_number=1")).andExpect(status().isOk());
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldReturnArticlePageWithCommentForArticlePageGetRequest() throws Exception {
        this.mockMvc.perform(get(ARTICLE_PAGE_URL + "?article_number=1")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Some impressive comment.")));
    }

    @WithMockUser(authorities = {SALE_AUTHORITY_NAME})
    @Test
    public void shouldReturnAddArticlePageForGetRequest() throws Exception {
        mockMvc.perform(get(ADD_ARTICLE_PAGE_URL)).andExpect(status().isOk());
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldReturnRedirectToArticlesPageForSuccessfulAddPostRequest() throws Exception {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("Title");
        articleDTO.setContent("Content");
        this.mockMvc.perform(post("/private/articles/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", articleDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles?added"));
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldAddAndDeleteArticlePageAndReturnRedirectToArticlesPage() throws Exception {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("Title");
        articleDTO.setContent("Content");
        this.mockMvc.perform(post(ADD_ARTICLE_PAGE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", articleDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles?added"));
        this.mockMvc.perform(post(DELETE_ARTICLE_URL + "?article_id=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles?page=1&deleted"));
    }

    @WithMockUser(authorities = {SALE_AUTHORITY_NAME})
    @Test
    public void shouldReturnEditArticlePageForGetRequest() throws Exception {
        mockMvc.perform(get(EDIT_ARTICLE_URL + "?article_number=1")).andExpect(status().isOk());
    }

    @WithUserDetails("sale@sale.com")
    @Test
    public void shouldEditArticleAndReturnRedirectToArticlesPage() throws Exception {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle("New title");
        articleDTO.setContent("New content");
        this.mockMvc.perform(post(EDIT_ARTICLE_URL + "?article_number=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("article", articleDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(ARTICLE_PAGE_URL + "?article_number=1"));
    }
}
