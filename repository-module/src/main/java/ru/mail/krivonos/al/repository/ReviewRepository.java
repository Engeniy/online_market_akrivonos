package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    List<Review> findNotHiddenReviews(int limit, int offset);

    int getCountOfNotHiddenEntities();
}
