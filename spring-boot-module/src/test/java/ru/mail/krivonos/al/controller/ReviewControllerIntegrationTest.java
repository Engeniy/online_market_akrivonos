package ru.mail.krivonos.al.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.krivonos.al.controller.model.ReviewDTOForm;
import ru.mail.krivonos.al.service.model.ReviewDTO;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private UserDTO correctUserDTO;

    @Before
    public void init() {
        correctUserDTO = new UserDTO();
        correctUserDTO.setEmail("krivonos-al@mail.ru");
        correctUserDTO.setName("Alex");
        correctUserDTO.setSurname("Krivonos");
        RoleDTO correctRole = new RoleDTO();
        correctRole.setName("Sale User");
        correctUserDTO.setRole(correctRole);
    }

    @Test
    public void shouldReturnReviewsPageForGetRequestWithPageNumber() throws Exception {
        mockMvc.perform(get("/reviews/1")).andExpect(status().isOk());
    }

    @Test
    public void requestForReviewsPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/reviews/1")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldRedirectToReviewsPageForGetRequestWithoutPageNumber() throws Exception {
        mockMvc.perform(get("/reviews"))
                .andExpect(redirectedUrl("/reviews/1"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldSendRedirectToReviewsFirstPageWithPositiveParamForSuccessfulDeleteReviewPostRequest()
            throws Exception {
        this.mockMvc.perform(post("/reviews/1/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews/1?deleted"));
    }

    @Test
    public void shouldSendRedirectToReviewsFirstPageWithNegativeParamForUnsuccessfulDeleteReviewPostRequest()
            throws Exception {
        this.mockMvc.perform(post("/reviews/100/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews/1?deleted_zero"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToReviewsFirstPageWithPositiveParamForSuccessfulUpdateReviewsRequest()
            throws Exception {
        ReviewDTOForm reviewDTOForm = new ReviewDTOForm();
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setHidden(true);
        reviewDTOs.add(reviewDTO);
        reviewDTOForm.setReviewList(reviewDTOs);
        this.mockMvc.perform(post("/reviews/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("id", 1L)
                .flashAttr("reviews", reviewDTOForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews/1?updated"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToReviewsFirstPageWithNegativeParamForUnsuccessfulUpdateReviewsRequest()
            throws Exception {
        ReviewDTOForm reviewDTOForm = new ReviewDTOForm();
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        reviewDTOForm.setReviewList(reviewDTOs);
        this.mockMvc.perform(post("/reviews/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("reviews", reviewDTOForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews/1?updated_zero"));
    }
}
