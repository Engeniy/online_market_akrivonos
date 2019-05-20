package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ReviewDTO;

import static ru.mail.krivonos.al.controller.constant.PageConstants.REVIEWS_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PAGE_NUMBER_PARAM;
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
    public String getReviewPageWithPageNumber(
            @RequestParam(name = "page", defaultValue = "1") Integer page, Model model
    ) {
        PageDTO<ReviewDTO> pageDTO = reviewService.getReviews(page);
        model.addAttribute("page", pageDTO);
        return REVIEWS_PAGE;
    }

    @PostMapping(REVIEWS_DELETE_URL)
    public String deleteReview(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber
    ) {
        reviewService.deleteReviewByID(id);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, REVIEWS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, DELETED_POSITIVE_PARAM);
    }

    @PostMapping(REVIEWS_UPDATE_URL)
    public String updateReviews(
            @ModelAttribute("reviews") PageDTO<ReviewDTO> pageDTO,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber
    ) {
        if (pageDTO.getList().isEmpty()) {
            reviewService.updateHiddenStatus(pageDTO.getList());
            return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, REVIEWS_PAGE_URL, PAGE_NUMBER_PARAM,
                    pageNumber, UPDATED_NEGATIVE_PARAM);
        }
        reviewService.updateHiddenStatus(pageDTO.getList());
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, REVIEWS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, UPDATED_POSITIVE_PARAM);
    }
}
