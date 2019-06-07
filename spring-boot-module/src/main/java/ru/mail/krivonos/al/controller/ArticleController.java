package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.CommentDTO;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import javax.validation.Valid;

import static ru.mail.krivonos.al.controller.constant.AttributeConstants.ARTICLE_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.COMMENT_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.PAGE_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.AttributeConstants.USER_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ADD_ARTICLE_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLES_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLE_EDIT_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLE_PAGE;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.ARTICLE_ID_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.ARTICLE_NUMBER_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.PAGE_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ADD_ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.DELETE_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.EDIT_ARTICLE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ADDED_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ARTICLE_NUMBER_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PAGE_NUMBER_PARAM;

@Controller("articleController")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(ARTICLES_PAGE_URL)
    public String getArticles(
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, Model model
    ) {
        model.addAttribute(USER_ATTRIBUTE, userPrincipal);
        PageDTO<ArticleDTO> pageDTO = articleService.getArticles(pageNumber);
        model.addAttribute(PAGE_ATTRIBUTE, pageDTO);
        return ARTICLES_PAGE;
    }

    @GetMapping(ARTICLE_PAGE_URL)
    public String getArticle(
            @RequestParam(name = ARTICLE_NUMBER_PARAMETER) Long id,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, Model model
    ) {
        model.addAttribute(USER_ATTRIBUTE, userPrincipal);
        ArticleDTO articleDTO = articleService.getArticleById(id);
        model.addAttribute(ARTICLE_ATTRIBUTE, articleDTO);
        model.addAttribute(COMMENT_ATTRIBUTE, new CommentDTO());
        return ARTICLE_PAGE;
    }

    @GetMapping(ADD_ARTICLE_PAGE_URL)
    public String getAddArticlePage(
            Model model
    ) {
        model.addAttribute(ARTICLE_ATTRIBUTE, new ArticleDTO());
        return ADD_ARTICLE_PAGE;
    }

    @PostMapping(ADD_ARTICLE_PAGE_URL)
    public String saveArticle(
            @ModelAttribute(ARTICLE_ATTRIBUTE) @Valid ArticleDTO articleDTO,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ADD_ARTICLE_PAGE;
        }
        setAuthorAndAdd(userPrincipal.getUserID(), articleDTO);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, ARTICLES_PAGE_URL, ADDED_PARAM);
    }

    @PostMapping(DELETE_ARTICLE_URL)
    public String deleteArticle(
            @RequestParam(ARTICLE_ID_PARAMETER) Long articleId,
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber
    ) {
        articleService.deleteArticle(articleId);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ARTICLES_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, DELETED_POSITIVE_PARAM);
    }

    @GetMapping(EDIT_ARTICLE_URL)
    public String getArticleEditPage(
            @RequestParam(ARTICLE_NUMBER_PARAMETER) Long articleId, Model model
    ) {
        ArticleDTO article = articleService.getArticleById(articleId);
        model.addAttribute(ARTICLE_ATTRIBUTE, article);
        return ARTICLE_EDIT_PAGE;
    }

    @PostMapping(EDIT_ARTICLE_URL)
    public String updateArticle(
            @ModelAttribute(ARTICLE_ATTRIBUTE) @Valid ArticleDTO articleDTO, BindingResult bindingResult,
            @RequestParam(ARTICLE_NUMBER_PARAMETER) Long articleId
    ) {
        if (bindingResult.hasErrors()) {
            return ARTICLE_EDIT_PAGE;
        }
        articleDTO.setId(articleId);
        articleService.update(articleDTO);
        return String.format(REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE, ARTICLE_PAGE_URL, ARTICLE_NUMBER_PARAM,
                articleDTO.getId());
    }

    private void setAuthorAndAdd(Long authorId, ArticleDTO articleDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(authorId);
        articleDTO.setAuthor(userDTO);
        articleService.add(articleDTO);
    }
}
