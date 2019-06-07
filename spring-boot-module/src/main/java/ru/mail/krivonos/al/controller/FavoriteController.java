package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.repository.model.FavoriteArticleKey;
import ru.mail.krivonos.al.service.FavoriteArticleService;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.FavoriteArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import static ru.mail.krivonos.al.controller.constant.AttributeConstants.PAGE_ATTRIBUTE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.FAVORITE_ARTICLES_PAGE;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.ARTICLE_ID_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.PAGE_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.USER_ID_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.FAVORITE_ARTICLES_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.FAVORITE_ADDED_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PAGE_NUMBER_PARAM;

@Controller("favoriteController")
public class FavoriteController {

    private final FavoriteArticleService favoriteArticleService;

    @Autowired
    public FavoriteController(FavoriteArticleService favoriteArticleService) {
        this.favoriteArticleService = favoriteArticleService;
    }

    @GetMapping(FAVORITE_ARTICLES_URL)
    public String getFavoriteArticles(
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer page,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, Model model
    ) {
        PageDTO<FavoriteArticleDTO> pageDTO = favoriteArticleService.getArticlesByUserId(page, userPrincipal.getUserID());
        model.addAttribute(PAGE_ATTRIBUTE, pageDTO);
        return FAVORITE_ARTICLES_PAGE;
    }

    @PostMapping(FAVORITE_ARTICLES_URL)
    public String addFavoriteArticle(
            @RequestParam(name = USER_ID_PARAMETER) Long userId,
            @RequestParam(name = ARTICLE_ID_PARAMETER) Long articleId,
            @RequestParam(name = PAGE_PARAMETER, defaultValue = "1") Integer pageNumber
    ) {
        FavoriteArticleKey favoriteArticleKey = new FavoriteArticleKey();
        favoriteArticleKey.setUserId(userId);
        favoriteArticleKey.setArticleId(articleId);
        favoriteArticleService.addFavoriteArticle(favoriteArticleKey);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, ARTICLES_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, FAVORITE_ADDED_PARAM);
    }
}
