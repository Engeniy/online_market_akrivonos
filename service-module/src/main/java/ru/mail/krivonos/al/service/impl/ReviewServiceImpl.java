package ru.mail.krivonos.al.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.repository.ReviewRepository;
import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.converter.ReviewConverter;
import ru.mail.krivonos.al.service.exceptions.ConnectionAutoCloseException;
import ru.mail.krivonos.al.service.exceptions.ReviewServiceException;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.repository.constant.LimitConstants.USERS_LIMIT;
import static ru.mail.krivonos.al.service.constant.ServiceMessageConstants.CONNECTION_CLOSE_ERROR_MESSAGE;
import static ru.mail.krivonos.al.service.constant.ServiceMessageConstants.PAGES_COUNTING_EXCEPTION_MESSAGE;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {

    private static final String REVIEWS_GETTING_EXCEPTION_MESSAGE = "Error while getting reviews list from data source.";
    private static final String REVIEW_DELETING_EXCEPTION_MESSAGE = "Error while deleting review.";
    private static final String HIDDEN_STATUS_UPDATE_ERROR_MESSAGE = "Error while updating hidden status.";

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final PageCountingService pageCountingService;

    @Autowired
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ReviewConverter reviewConverter,
            PageCountingService pageCountingService
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.pageCountingService = pageCountingService;
    }

    @Override
    public List<ReviewDTO> getReviews(int pageNumber) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Review> reviews = reviewRepository.findReviews(connection, pageNumber);
                List<ReviewDTO> reviewDTOs = getReviewDTOs(reviews);
                connection.commit();
                return reviewDTOs;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(REVIEWS_GETTING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int getPagesNumber() {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int reviewsNumber = reviewRepository.getCountOfReviews(connection);
                int pagesNumber = pageCountingService.countPages(reviewsNumber, USERS_LIMIT);
                connection.commit();
                return pagesNumber;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(PAGES_COUNTING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int updateHiddenStatus(List<ReviewDTO> reviews) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Review> reviewList = getReviews(reviews);
                int updated = reviewRepository.updateHiddenStatus(connection, reviewList);
                connection.commit();
                return updated;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(HIDDEN_STATUS_UPDATE_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int deleteReviewByID(Long id) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int deleted = reviewRepository.deleteReviewByID(connection, id);
                connection.commit();
                return deleted;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(REVIEW_DELETING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    private List<ReviewDTO> getReviewDTOs(List<Review> reviews) {
        return reviews.stream()
                .map(reviewConverter::toDTO)
                .collect(Collectors.toList());
    }

    private List<Review> getReviews(List<ReviewDTO> reviews) {
        return reviews.stream()
                .map(reviewConverter::fromDTO)
                .collect(Collectors.toList());
    }
}
