package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.service.converter.ReviewConverter;
import ru.mail.krivonos.al.service.converter.UserConverterAggregator;
import ru.mail.krivonos.al.service.model.ReviewDTO;

@Component("reviewConverter")
public class ReviewConverterImpl implements ReviewConverter {

    private final UserConverterAggregator userConverterAggregator;

    @Autowired
    public ReviewConverterImpl(UserConverterAggregator userConverterAggregator) {
        this.userConverterAggregator = userConverterAggregator;
    }

    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setAuthor(userConverterAggregator.getAuthorConverter().toDTO(review.getAuthor()));
        reviewDTO.setReview(review.getReview());
        reviewDTO.setDateOfCreation(review.getDateOfCreation());
        reviewDTO.setHidden(review.isHidden());
        return reviewDTO;
    }

    @Override
    public Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setAuthor(userConverterAggregator.getAuthorConverter().toEntity(reviewDTO.getAuthor()));
        review.setReview(reviewDTO.getReview());
        review.setHidden(reviewDTO.isHidden());
        review.setDateOfCreation(reviewDTO.getDateOfCreation());
        return review;
    }
}
