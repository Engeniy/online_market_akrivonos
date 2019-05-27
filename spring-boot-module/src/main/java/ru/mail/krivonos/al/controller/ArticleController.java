package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

import static ru.mail.krivonos.al.controller.constant.PageConstants.ADD_ARTICLE_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLES_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLE_EDIT_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLE_PAGE;
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
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber,
            Authentication authentication, Model model
    ) {
        if (authentication != null) {
            AuthUserPrincipal userPrincipal = (AuthUserPrincipal) authentication.getPrincipal();
            model.addAttribute("user", userPrincipal);
        }
        PageDTO<ArticleDTO> pageDTO = articleService.getArticles(pageNumber);
        model.addAttribute("page", pageDTO);
        return ARTICLES_PAGE;
    }

    @GetMapping(ARTICLE_PAGE_URL)
    public String getArticle(
            @RequestParam(name = "article_number") Long id,
            Authentication authentication, Model model
    ) {
        if (authentication != null) {
            AuthUserPrincipal userPrincipal = (AuthUserPrincipal) authentication.getPrincipal();
            model.addAttribute("user", userPrincipal);
        }
        ArticleDTO articleDTO = articleService.getArticleById(id);
        model.addAttribute("article", articleDTO);
        model.addAttribute("comment", new CommentDTO());
        return ARTICLE_PAGE;
    }

    @GetMapping(ADD_ARTICLE_PAGE_URL)
    public String getAddArticlePage(
            Model model
    ) {
        model.addAttribute("article", new ArticleDTO());
        return ADD_ARTICLE_PAGE;
    }

    @PostMapping(ADD_ARTICLE_PAGE_URL)
    public String saveArticle(
            @ModelAttribute("article") @Valid ArticleDTO articleDTO,
            BindingResult bindingResult, Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            return ADD_ARTICLE_PAGE;
        }
        AuthUserPrincipal userPrincipal = (AuthUserPrincipal) authentication.getPrincipal();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userPrincipal.getUserID());
        articleDTO.setAuthor(userDTO);
        articleService.add(articleDTO);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, ARTICLES_PAGE_URL, ADDED_PARAM);
    }

    @PostMapping(DELETE_ARTICLE_URL)
    public String deleteArticle(
            @RequestParam("article_id") Long articleId,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber
    ) {
        articleService.deleteArticle(articleId);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ARTICLES_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, DELETED_POSITIVE_PARAM);
    }

    @GetMapping(EDIT_ARTICLE_URL)
    public String getArticleEditPage(
            @RequestParam("article_number") Long articleId, Model model
    ) {
        ArticleDTO article = articleService.getArticleById(articleId);
        model.addAttribute("article", article);
        return ARTICLE_EDIT_PAGE;
    }

    @PostMapping(EDIT_ARTICLE_URL)
    public String updateArticle(
            @ModelAttribute("article") @Valid ArticleDTO articleDTO, BindingResult bindingResult,
            @RequestParam("article_number") Long articleId
    ) {
        if (bindingResult.hasErrors()) {
            return ARTICLE_EDIT_PAGE;
        }
        articleDTO.setId(articleId);
        articleService.update(articleDTO);
        return String.format(REDIRECT_WITH_DECIMAL_PARAMETER_TEMPLATE, ARTICLE_PAGE_URL, ARTICLE_NUMBER_PARAM,
                articleDTO.getId());
    }
}
