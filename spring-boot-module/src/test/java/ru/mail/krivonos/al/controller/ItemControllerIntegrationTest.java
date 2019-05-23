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
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ADD_ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.DELETE_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.EDIT_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_PAGE_URL;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(authorities = {SALE_AUTHORITY_NAME})
    @Test
    public void shouldReturnItemsPageForGetRequest() throws Exception {
        mockMvc.perform(get(ITEMS_PAGE_URL)).andExpect(status().isOk());
    }

    @Test
    public void requestForItemsPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get(ITEMS_PAGE_URL)
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Cat collar")));
    }
}
