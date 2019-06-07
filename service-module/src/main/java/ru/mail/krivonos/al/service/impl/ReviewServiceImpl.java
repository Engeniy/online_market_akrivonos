package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ReviewRepository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.Review;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.converter.ReviewConverter;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.REVIEWS_LIMIT;
import static ru.mail.krivonos.al.service.constant.OrderConstants.DATE_OF_CREATION;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final UserRepository userRepository;
    private final PageCountingService pageCountingService;

    @Autowired
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ReviewConverter reviewConverter,
            UserRepository userRepository,
            PageCountingService pageCountingService
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.userRepository = userRepository;
        this.pageCountingService = pageCountingService;
    }

    @Override
    @Transactional
    public PageDTO<ReviewDTO> getReviews(int pageNumber) {
        PageDTO<ReviewDTO> pageDTO = new PageDTO<>();
        int countOfEntities = reviewRepository.getCountOfEntities();
        int offset = getOffsetAndSetPages(pageDTO, pageNumber, countOfEntities);
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
            byId.setHidden(review.isHidden());
            reviewRepository.merge(byId);
        }
    }

    @Override
    @Transactional
    public void deleteReviewByID(Long id) {
        Review byId = reviewRepository.findById(id);
        reviewRepository.remove(byId);
    }

    @Override
    @Transactional
    public void add(ReviewDTO reviewDTO) {
        Review review = reviewConverter.toEntity(reviewDTO);
        review.setDateOfCreation(new Date());
        User user = userRepository.findByIdNotDeleted(reviewDTO.getAuthor().getId());
        review.setAuthor(user);
        reviewRepository.persist(review);
    }

    private List<ReviewDTO> getReviewDTOs(List<Review> reviews) {
        return reviews.stream()
                .map(reviewConverter::toDTO)
                .collect(Collectors.toList());
    }

    private int getOffsetAndSetPages(PageDTO<ReviewDTO> pageDTO, Integer pageNumber, int countOfEntities) {
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, REVIEWS_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        return pageCountingService.getOffset(currentPageNumber, REVIEWS_LIMIT);
    }
}
