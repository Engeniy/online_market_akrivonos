package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.CommentService;
import ru.mail.krivonos.al.service.model.CommentDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import javax.validation.Valid;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLE_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_ADD_COMMENT_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_DELETE_COMMENT_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ARTICLE_NUMBER_PARAM;

@Controller("commentController")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(ARTICLE_ADD_COMMENT_URL)
    public String addComment(
            @RequestParam(name = "article_number") Long articleId,
            @RequestParam(name = "author_id") Long authorId,
            @ModelAttribute("comment") @Valid CommentDTO commentDTO, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ARTICLE_PAGE;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(authorId);
        commentDTO.setAuthor(userDTO);
        commentService.addComment(articleId, commentDTO);
        return String.format(REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE, ARTICLE_PAGE_URL, ARTICLE_NUMBER_PARAM, articleId);
    }

    @PostMapping(ARTICLE_DELETE_COMMENT_URL)
    public String deleteComment(
            @RequestParam(name = "comment_id") Long commentId,
            @RequestParam(name = "article_number") Long articleId
    ) {
        commentService.deleteComment(commentId);
        return String.format(REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE, ARTICLE_PAGE_URL, ARTICLE_NUMBER_PARAM, articleId);
    }
}
