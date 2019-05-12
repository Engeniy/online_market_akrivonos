package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getReviews(int pageNumber);

    int getPagesNumber();

    int updateHiddenStatus(List<ReviewDTO> reviews);

    int deleteReviewByID(Long id);
}
