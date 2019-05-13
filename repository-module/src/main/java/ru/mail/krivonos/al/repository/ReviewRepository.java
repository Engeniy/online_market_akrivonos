package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.Review;

import java.sql.Connection;
import java.util.List;

public interface ReviewRepository extends GenericRepository {

    List<Review> findReviews(Connection connection, int pageNumber);

    int getCountOfReviews(Connection connection);

    int updateHiddenStatus(Connection connection, List<Review> reviews);

    int deleteReviewByID(Connection connection, Long id);
}
