package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.ReviewRepository;
import ru.mail.krivonos.al.repository.exceptions.ReviewRepositoryException;
import ru.mail.krivonos.al.repository.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.mail.krivonos.al.repository.constant.LimitConstants.REVIEWS_LIMIT;
import static ru.mail.krivonos.al.repository.constant.RepositoryMessageConstants.QUERY_EXECUTION_ERROR_MESSAGE;
import static ru.mail.krivonos.al.repository.constant.RepositoryMessageConstants.RESULT_SET_CLOSING_ERROR_MESSAGE;

@Repository("reviewRepository")
public class ReviewRepositoryImpl extends GenericRepositoryImpl implements ReviewRepository {

    private static final String REVIEW_EXTRACTION_ERROR_MESSAGE = "Error while extracting review.";
    private static final String REVIEWS_COUNTING_ERROR_MESSAGE = "Error while counting reviews.";
    private static final String HIDDEN_STATUS_UPDATE_ERROR_MESSAGE = "Error while updating hidden status.";

    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    public List<Review> findReviews(Connection connection, int pageNumber) {
        String sql = "SELECT id, name, surname, patronymic, review, date_of_creation, hidden FROM Review " +
                "WHERE deleted = FALSE ORDER BY date_of_creation LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, REVIEWS_LIMIT);
            preparedStatement.setInt(2, getOffset(pageNumber));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Review> reviews = new ArrayList<>();
                while (resultSet.next()) {
                    Review review = getReview(resultSet);
                    reviews.add(review);
                }
                return reviews;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new ReviewRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public int getCountOfReviews(Connection connection) {
        String sql = "SELECT COUNT(*) FROM Review WHERE deleted = FALSE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getReviewsNumber(resultSet);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new ReviewRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public int updateHiddenStatus(Connection connection, List<Review> reviews) {
        String sql = "UPDATE Review SET hidden = ? WHERE id = ? AND hidden <> ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            addQueries(reviews, preparedStatement);
            int[] batchExecutionResult = preparedStatement.executeBatch();
            return countUpdatedRows(batchExecutionResult);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(HIDDEN_STATUS_UPDATE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int deleteReviewByID(Connection connection, Long id) {
        String sql = "UPDATE Review SET deleted = TRUE WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    private Review getReview(ResultSet resultSet) {
        try {
            Review review = new Review();
            review.setId(resultSet.getLong("id"));
            review.setName(resultSet.getString("name"));
            review.setSurname(resultSet.getString("surname"));
            review.setPatronymic(resultSet.getString("patronymic"));
            review.setReview(resultSet.getString("review"));
            review.setDateOfCreation(new Date(resultSet.getTimestamp("date_of_creation").getTime()));
            review.setHidden(resultSet.getBoolean("hidden"));
            return review;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(REVIEW_EXTRACTION_ERROR_MESSAGE, e);
        }
    }

    private int getOffset(int pageNumber) {
        return (pageNumber - 1) * REVIEWS_LIMIT;
    }

    private int getReviewsNumber(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(REVIEWS_COUNTING_ERROR_MESSAGE, e);
        }
        return 0;
    }

    private void addQueries(List<Review> reviews, PreparedStatement preparedStatement) throws SQLException {
        for (Review review : reviews) {
            preparedStatement.setBoolean(1, review.getHidden());
            preparedStatement.setLong(2, review.getId());
            preparedStatement.setBoolean(3, review.getHidden());
            preparedStatement.addBatch();
        }
    }

    private int countUpdatedRows(int[] batchExecutionResult) {
        int updatedRows = 0;
        for (int result : batchExecutionResult) {
            updatedRows += result;
        }
        return updatedRows;
    }
}
