package ru.mail.krivonos.al.controller;

import org.junit.Before;
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
import ru.mail.krivonos.al.controller.model.ReviewForm;
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

    @WithUserDetails("admin@admin.com")
    @Test
    public void shouldReturnReviewsPageForGetRequestWithPageNumber() throws Exception {
        mockMvc.perform(get("/private/reviews")).andExpect(status().isOk());
    }

    @WithUserDetails("admin@admin.com")
    @Test
    public void requestForReviewsPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/private/reviews")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @WithUserDetails("admin@admin.com")
    @Test
    public void shouldSendRedirectToReviewsFirstPageWithPositiveParamForSuccessfulUpdateReviewsRequest()
            throws Exception {
        ReviewForm reviewForm = new ReviewForm();
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setHidden(true);
        reviewDTOs.add(reviewDTO);
        reviewForm.setReviewList(reviewDTOs);
        this.mockMvc.perform(post("/private/reviews/update?page=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("id", 1L)
                .flashAttr("reviews", reviewForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/reviews?page=1&updated"));
    }

    @WithUserDetails("admin@admin.com")
    @Test
    public void shouldSendRedirectToReviewsFirstPageWithNegativeParamForUnsuccessfulUpdateReviewsRequest()
            throws Exception {
        ReviewForm reviewForm = new ReviewForm();
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        reviewForm.setReviewList(reviewDTOs);
        this.mockMvc.perform(post("/private/reviews/update?page=1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("reviews", reviewForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/reviews?page=1&updated_zero"));
    }

    @WithUserDetails("customer@customer.com")
    @Test
    public void shouldSendRedirectToReviewsPageWithPositiveParamForSuccessfulAddReviewRequest()
            throws Exception {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReview("Some review.");
        this.mockMvc.perform(post("/private/reviews/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("review", reviewDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/reviews/add?added"));
    }
}
