package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    PageDTO<ReviewDTO> getReviews(int pageNumber);

    void updateHiddenStatus(List<ReviewDTO> reviews);

    void deleteReviewByID(Long id);

    void add(ReviewDTO reviewDTO);

    PageDTO<ReviewDTO> getNotHiddenReviews(int pageNumber);
}
