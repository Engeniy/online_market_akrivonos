package ru.mail.krivonos.al.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.krivonos.al.service.model.CommentDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_ADD_COMMENT_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_DELETE_COMMENT_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.FAVORITE_ARTICLES_URL;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FavoriteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldReturnRedirectToArticlesPageWithPositiveParamAfterAddFavoriteRequest() throws Exception {
        this.mockMvc.perform(post(FAVORITE_ARTICLES_URL + "?user_id=1&article_id=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles?page=1&favorite_added"));
    }

    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldReturnFavoriteArticlesPageForGetRequest() throws Exception {
        this.mockMvc.perform(post(FAVORITE_ARTICLES_URL + "?user_id=4&article_id=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles?page=1&favorite_added"));
        this.mockMvc.perform(get(FAVORITE_ARTICLES_URL)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Some summary")));
    }
}
