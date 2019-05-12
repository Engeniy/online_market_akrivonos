package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mail.krivonos.al.controller.model.ReviewDTOForm;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.mail.krivonos.al.controller.constant.PageConstants.REVIEWS_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_FIRST_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_PAGE_WITH_PAGE_NUMBER_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_NEGATIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPDATED_NEGATIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPDATED_POSITIVE_PARAM;

@Controller("reviewController")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(REVIEWS_PAGE_URL)
    public String getReviewPage() {
        return String.format(REDIRECT_TEMPLATE, REVIEWS_FIRST_PAGE_URL);
    }

    @GetMapping(REVIEWS_PAGE_WITH_PAGE_NUMBER_URL)
    public String getReviewPageWithPageNumber(
            @PathVariable("page") Integer page, Model model
    ) {
        Optional<Integer> pageOptional = Optional.ofNullable(page);
        int pageNumber = pageOptional.orElse(1);
        int pagesNumber = reviewService.getPagesNumber();
        if (pageNumber > pagesNumber && pagesNumber > 0) {
            pageNumber = pagesNumber;
        }
        List<Integer> pagesNumbers = IntStream
                .rangeClosed(1, pagesNumber)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pages", pagesNumbers);
        model.addAttribute("current_page", pageNumber);
        List<ReviewDTO> reviews = reviewService.getReviews(pageNumber);
        ReviewDTOForm reviewDTOForm = new ReviewDTOForm();
        reviewDTOForm.setReviewList(reviews);
        model.addAttribute("reviews", reviewDTOForm);
        return REVIEWS_PAGE;
    }

    @PostMapping(REVIEWS_DELETE_URL)
    public String deleteReview(
            @PathVariable("id") Long id
    ) {
        int deleted = reviewService.deleteReviewByID(id);
        if (deleted == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, REVIEWS_FIRST_PAGE_URL, DELETED_NEGATIVE_PARAM);
        }

        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, REVIEWS_FIRST_PAGE_URL, DELETED_POSITIVE_PARAM);
    }

    @PostMapping(REVIEWS_UPDATE_URL)
    public String updateReviews(
            @ModelAttribute("reviews") ReviewDTOForm reviewDTOForm
    ) {
        int updated = reviewService.updateHiddenStatus(reviewDTOForm.getReviewList());
        if (updated == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, REVIEWS_FIRST_PAGE_URL, UPDATED_NEGATIVE_PARAM);
        }
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, REVIEWS_FIRST_PAGE_URL, UPDATED_POSITIVE_PARAM);
    }
}
