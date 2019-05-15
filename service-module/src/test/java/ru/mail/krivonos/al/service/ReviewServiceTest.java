package ru.mail.krivonos.al.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.mail.krivonos.al.repository.ReviewRepository;
import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.service.converter.ReviewConverter;
import ru.mail.krivonos.al.service.exceptions.ReviewServiceException;
import ru.mail.krivonos.al.service.impl.ReviewServiceImpl;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @Mock
    private Connection connection;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewConverter reviewConverter;
    @Mock
    private PageCountingService pageCountingService;

    private ReviewService reviewService;

    @Before
    public void init() {
        reviewService = new ReviewServiceImpl(reviewRepository, reviewConverter, pageCountingService);
        when(reviewRepository.getConnection()).thenReturn(connection);
    }


    @Test
    public void shouldGetPageNumberAndReturnReviewDTOListForGetReviewsMethodCall() {
        Integer pageNumber = 1;
        Review review = new Review();
        ReviewDTO reviewDTO = new ReviewDTO();
        List<Review> reviews = Collections.singletonList(review);
        when(reviewRepository.findReviews(connection, pageNumber)).thenReturn(reviews);
        when(reviewConverter.toDTO(review)).thenReturn(reviewDTO);
        List<ReviewDTO> returnedReviews = reviewService.getReviews(pageNumber);
        assertEquals(reviewDTO, returnedReviews.get(0));
    }

    @Test(expected = ReviewServiceException.class)
    public void shouldThrowReviewServiceExceptionWhenCatchingExceptionFromRepositoryForGetReviews() {
        Integer pageNumber = 1;
        when(reviewRepository.findReviews(connection, pageNumber)).thenThrow(new RuntimeException());
        reviewService.getReviews(pageNumber);
    }

    @Test
    public void shouldReturnUpdatedNumberForUpdateHiddenStatusMethodCall() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Review review = new Review();
        List<ReviewDTO> reviews = Collections.singletonList(reviewDTO);
        int updated = 1;
        when(reviewConverter.fromDTO(reviewDTO)).thenReturn(review);
        when(reviewRepository.updateHiddenStatus(any(), anyList())).thenReturn(updated);
        int result = reviewService.updateHiddenStatus(reviews);
        assertEquals(updated, result);
    }

    @Test(expected = ReviewServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForUpdateHiddenStatusMethodCall() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Review review = new Review();
        List<ReviewDTO> reviews = Collections.singletonList(reviewDTO);
        when(reviewConverter.fromDTO(reviewDTO)).thenReturn(review);
        when(reviewRepository.updateHiddenStatus(any(), anyList())).thenThrow(new RuntimeException());
        reviewService.updateHiddenStatus(reviews);
    }

    @Test
    public void shouldReturnDeletedNumberForDeleteReviewByIDMethodCall() {
        Long id = 1L;
        int deleted = 1;
        when(reviewRepository.deleteReviewByID(connection, id)).thenReturn(deleted);
        int result = reviewService.deleteReviewByID(id);
        assertEquals(deleted, result);
    }

    @Test(expected = ReviewServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForDeleteReviewByIDMethodCall() {
        Long id = 1L;
        when(reviewRepository.deleteReviewByID(connection, id)).thenThrow(new RuntimeException());
        reviewService.deleteReviewByID(id);
    }

    @Test
    public void shouldReturnPagesNumberFromGetPagesNumberMethod() {
        int reviewsNumber = 20;
        int limit = 10;
        int pagesNumber = 2;
        when(reviewRepository.getCountOfReviews(connection)).thenReturn(reviewsNumber);
        when(pageCountingService.countPages(reviewsNumber, limit)).thenReturn(pagesNumber);
        int result = reviewService.getPagesNumber();
        assertEquals(pagesNumber, result);
    }

    @Test(expected = ReviewServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryFromGetPagesNumberMethod() {
        int reviewsNumber = 20;
        int limit = 10;
        when(reviewRepository.getCountOfReviews(connection)).thenReturn(reviewsNumber);
        when(pageCountingService.countPages(reviewsNumber, limit)).thenThrow(new RuntimeException());
        reviewService.getPagesNumber();
    }
}
