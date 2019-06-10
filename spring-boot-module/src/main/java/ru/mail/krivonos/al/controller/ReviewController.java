package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.controller.model.ReviewForm;
import ru.mail.krivonos.al.service.ReviewService;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ReviewDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import javax.validation.Valid;

import static ru.mail.krivonos.al.controller.constant.AttributeConstants.PAGE_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.REVIEWS_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.REVIEW_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ADD_REVIEW_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.REVIEWS_PAGE;
import static ru.mail.krivonos.al.controller.constant.PathVariableConstants.ID_VARIABLE;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.PAGE_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_ADD_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REVIEWS_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ADDED_PARAM;
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
    public String getReviews(
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber, Model model
    ) {
        PageDTO<ReviewDTO> page = reviewService.getReviews(pageNumber);
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setReviewList(page.getList());
        model.addAttribute(REVIEWS_ATTRIBUTE, reviewForm);
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return REVIEWS_PAGE;
    }

    @PostMapping(REVIEWS_DELETE_URL)
    public String deleteReview(
            @PathVariable(ID_VARIABLE) Long id,
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber
    ) {
        reviewService.deleteReviewByID(id);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, REVIEWS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, DELETED_POSITIVE_PARAM);
    }

    @PostMapping(REVIEWS_UPDATE_URL)
    public String updateReviews(
            @ModelAttribute(REVIEWS_ATTRIBUTE) ReviewForm reviewForm,
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber
    ) {
        if (reviewForm.getReviewList().isEmpty()) {
            return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, REVIEWS_PAGE_URL, PAGE_NUMBER_PARAM,
                    pageNumber, UPDATED_NEGATIVE_PARAM);
        }
        reviewService.updateHiddenStatus(reviewForm.getReviewList());
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, REVIEWS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, UPDATED_POSITIVE_PARAM);
    }

    @GetMapping(REVIEWS_ADD_PAGE_URL)
    public String getAddReviewPage(Model model) {
        model.addAttribute(REVIEW_ATTRIBUTE, new ReviewDTO());
        return ADD_REVIEW_PAGE;
    }

    @PostMapping(REVIEWS_ADD_PAGE_URL)
    public String saveReview(
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal,
            @ModelAttribute(REVIEW_ATTRIBUTE) @Valid ReviewDTO reviewDTO, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ADD_REVIEW_PAGE;
        }
        setAuthorAndAdd(userPrincipal.getUserID(), reviewDTO);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, REVIEWS_ADD_PAGE_URL, ADDED_PARAM);
    }

    private void setAuthorAndAdd(Long authorId, ReviewDTO reviewDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(authorId);
        reviewDTO.setAuthor(userDTO);
        reviewService.add(reviewDTO);
    }
}
