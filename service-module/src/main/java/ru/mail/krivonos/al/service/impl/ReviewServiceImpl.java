package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ReviewRepository;
import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.converter.ReviewConverter;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.REVIEWS_LIMIT;
import static ru.mail.krivonos.al.service.constant.OrderConstants.DATE_OF_CREATION;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {

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
    @Transactional
    public PageDTO<ReviewDTO> getReviews(int pageNumber) {
        PageDTO<ReviewDTO> pageDTO = new PageDTO<>();
        int countOfEntities = reviewRepository.getCountOfEntities();
        int countOfPages = pageCountingService.countPages(countOfEntities, REVIEWS_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, REVIEWS_LIMIT);
        List<Review> reviews = reviewRepository.findAllWithDescendingOrder(REVIEWS_LIMIT, offset, DATE_OF_CREATION);
        List<ReviewDTO> reviewDTOs = getReviewDTOs(reviews);
        pageDTO.setList(reviewDTOs);
        return pageDTO;
    }

    @Override
    @Transactional
    public void updateHiddenStatus(List<ReviewDTO> reviews) {
        for (ReviewDTO review : reviews) {
            Review byId = reviewRepository.findById(review.getId());
            byId.setHidden(review.getHidden());
            reviewRepository.merge(byId);
        }
    }

    @Override
    @Transactional
    public void deleteReviewByID(Long id) {
        Review byId = reviewRepository.findById(id);
        reviewRepository.remove(byId);
    }

    private List<ReviewDTO> getReviewDTOs(List<Review> reviews) {
        return reviews.stream()
                .map(reviewConverter::toDTO)
                .collect(Collectors.toList());
    }
}
