package ru.mail.krivonos.al.service;

import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    PageDTO<ReviewDTO> getReviews(int pageNumber);

    void updateHiddenStatus(List<ReviewDTO> reviews);

    void deleteReviewByID(Long id);

    void add(ReviewDTO reviewDTO);

    @Transactional
    PageDTO<ReviewDTO> getNotHiddenReviews(int pageNumber);
}
