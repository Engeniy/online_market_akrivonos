package ru.mail.krivonos.al.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import ru.mail.krivonos.al.controller.model.ReviewDTOForm;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;
    @Mock
    private Model model;

    private ReviewController reviewController;

    @Before
    public void init() {
        reviewController = new ReviewController(reviewService);
    }

    @Test
    public void shouldReturnReviewsPageForReviewsGetRequestWithPageNumber() {
        String result = reviewController.getReviewPageWithPageNumber(1, model);
        assertEquals("reviews", result);
    }

    @Test
    public void shouldReturnRedirectToReviewsPageWithPositiveDeleteParamForSuccessfulDeleteRequest() {
        Long id = 1L;
        when(reviewService.deleteReviewByID(id)).thenReturn(1);
        String result = reviewController.deleteReview(id);
        assertEquals("redirect:/reviews?deleted", result);
    }

    @Test
    public void shouldReturnRedirectToReviewsPageWithNegativeDeleteParamForUnsuccessfulDeleteRequest() {
        Long id = 1L;
        when(reviewService.deleteReviewByID(id)).thenReturn(0);
        String result = reviewController.deleteReview(id);
        assertEquals("redirect:/reviews?deleted_zero", result);
    }

    @Test
    public void shouldReturnRedirectToReviewsPageWithPositiveUpdateParamForSuccessfulUpdateRequest() {
        ReviewDTOForm reviewDTOForm = new ReviewDTOForm();
        List<ReviewDTO> reviews = new ArrayList<>();
        ReviewDTO reviewDTO = new ReviewDTO();
        reviews.add(reviewDTO);
        reviewDTOForm.setReviewList(reviews);
        when(reviewService.updateHiddenStatus(reviews)).thenReturn(1);
        String result = reviewController.updateReviews(reviewDTOForm);
        assertEquals("redirect:/reviews?updated", result);
    }

    @Test
    public void shouldReturnRedirectToReviewsPageWithNegativeUpdateParamForUnsuccessfulUpdateRequest() {
        ReviewDTOForm reviewDTOForm = new ReviewDTOForm();
        List<ReviewDTO> reviews = new ArrayList<>();
        ReviewDTO reviewDTO = new ReviewDTO();
        reviews.add(reviewDTO);
        reviewDTOForm.setReviewList(reviews);
        when(reviewService.updateHiddenStatus(reviews)).thenReturn(0);
        String result = reviewController.updateReviews(reviewDTOForm);
        assertEquals("redirect:/reviews?updated_zero", result);
    }
}
