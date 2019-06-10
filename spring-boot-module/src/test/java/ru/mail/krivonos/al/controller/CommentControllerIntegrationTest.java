package ru.mail.krivonos.al.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_ADD_COMMENT_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_DELETE_COMMENT_URL;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldReturnRedirectToArticlePageAfterAddCommentRequest() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test comment");
        this.mockMvc.perform(post(ARTICLE_ADD_COMMENT_URL + "?article_number=1&author_id=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("comment", commentDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/public/article?article_number=1"));
    }

    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldReturnRedirectToArticlePageAfterDeleteCommentRequest() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("Test comment");
        this.mockMvc.perform(post(ARTICLE_ADD_COMMENT_URL + "?article_number=1&author_id=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("comment", commentDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/public/article?article_number=1"));
        this.mockMvc.perform(post(ARTICLE_DELETE_COMMENT_URL + "?article_number=1&comment_id=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("comment", commentDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/public/article?article_number=1"));
    }
}
