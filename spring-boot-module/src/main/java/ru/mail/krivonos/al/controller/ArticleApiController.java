package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.UserDTO;

import javax.validation.Valid;
import java.util.List;

import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_ARTICLES_URL;
import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_ARTICLES_WITH_ID_URL;

@RestController("articleApiController")
public class ArticleApiController {

    private final ArticleService articleService;

    @Autowired
    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(API_ARTICLES_URL)
    public ResponseEntity<List<ArticleDTO>> getArticles(
            @RequestParam(name = "limit", defaultValue = "10") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset
    ) {
        List<ArticleDTO> articles = articleService.getArticles(limit, offset);
        return new ResponseEntity(articles, HttpStatus.OK);
    }

    @GetMapping(API_ARTICLES_WITH_ID_URL)
    public ResponseEntity<ArticleDTO> getArticle(
            @PathVariable("id") Long id
    ) {
        ArticleDTO article = articleService.getArticle(id);
        return new ResponseEntity(article, HttpStatus.OK);
    }

    @PostMapping(API_ARTICLES_URL)
    public ResponseEntity<ArticleDTO> saveArticle(
            @RequestBody @Valid ArticleDTO articleDTO,
            Authentication authentication, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        AuthUserPrincipal userPrincipal = (AuthUserPrincipal) authentication.getPrincipal();
        UserDTO author = new UserDTO();
        author.setId(userPrincipal.getUserID());
        articleDTO.setAuthor(author);
        ArticleDTO returningArticle = articleService.add(articleDTO);
        return new ResponseEntity(returningArticle, HttpStatus.CREATED);
    }

    @DeleteMapping(API_ARTICLES_WITH_ID_URL)
    public ResponseEntity deleteArticle(
            @PathVariable("id") Long id
    ) {
        ArticleDTO articleDTO = articleService.deleteArticle(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
