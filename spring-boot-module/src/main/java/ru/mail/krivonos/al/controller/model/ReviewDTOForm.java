package ru.mail.krivonos.al.controller.model;

import ru.mail.krivonos.al.service.model.ReviewDTO;

import java.util.List;

public class ReviewDTOForm {

    private List<ReviewDTO> reviewList;

    public List<ReviewDTO> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewDTO> reviewList) {
        this.reviewList = reviewList;
    }
}
