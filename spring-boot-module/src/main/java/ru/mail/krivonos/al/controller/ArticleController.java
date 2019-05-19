package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.CommentDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLES_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLE_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLE_PAGE_URL;

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
        ArticleDTO articleDTO = articleService.getArticle(id);
        model.addAttribute("article", articleDTO);
        model.addAttribute("comment", new CommentDTO());
        return ARTICLE_PAGE;
    }
}
