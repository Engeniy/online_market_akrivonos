package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.service.converter.ReviewConverter;
import ru.mail.krivonos.al.service.model.ReviewDTO;

@Component("reviewConverter")
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setName(review.getName());
        reviewDTO.setSurname(review.getSurname());
        reviewDTO.setPatronymic(review.getPatronymic());
        reviewDTO.setReview(review.getReview());
        reviewDTO.setDateOfCreation(review.getDateOfCreation());
        reviewDTO.setHidden(review.getHidden());
        return reviewDTO;
    }

    @Override
    public Review fromDTO(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setName(reviewDTO.getName());
        review.setSurname(reviewDTO.getSurname());
        review.setPatronymic(reviewDTO.getPatronymic());
        review.setReview(reviewDTO.getReview());
        review.setHidden(reviewDTO.getHidden());
        review.setDateOfCreation(reviewDTO.getDateOfCreation());
        return review;
    }
}
