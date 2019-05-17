package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ARTICLES_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;

@Controller("articleController")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping(ARTICLES_PAGE_URL)
    public String getArticles(
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber, Model model
    ) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        PageDTO<ArticleDTO> pageDTO = articleService.getArticles(pageNumber);
        model.addAttribute("page", pageDTO);
        return ARTICLES_PAGE;
    }
}
