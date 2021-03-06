package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import static ru.mail.krivonos.al.controller.constant.PathVariableConstants.ID_VARIABLE;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.LIMIT_PARAMETER;
import static ru.mail.krivonos.al.controller.constant.RequestParameterConstants.OFFSET_PARAMETER;

@RestController("articleApiController")
public class ArticleApiController {

    private final ArticleService articleService;

    @Autowired
    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(API_ARTICLES_URL)
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<ArticleDTO>> getArticles(
            @RequestParam(name = LIMIT_PARAMETER, defaultValue = "10") Integer limit,
            @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") Integer offset
    ) {
        List<ArticleDTO> articles = articleService.getArticles(limit, offset);
        return new ResponseEntity(articles, HttpStatus.OK);
    }

    @GetMapping(API_ARTICLES_WITH_ID_URL)
    @SuppressWarnings("unchecked")
    public ResponseEntity<ArticleDTO> getArticle(
            @PathVariable(ID_VARIABLE) Long id
    ) {
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(article, HttpStatus.OK);
    }

    @PostMapping(API_ARTICLES_URL)
    @SuppressWarnings("unchecked")
    public ResponseEntity<ArticleDTO> saveArticle(
            @RequestBody @Valid ArticleDTO articleDTO,
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        ArticleDTO returningArticle = setAuthorAndAdd(userPrincipal.getUserID(), articleDTO);
        return new ResponseEntity(returningArticle, HttpStatus.CREATED);
    }

    @DeleteMapping(API_ARTICLES_WITH_ID_URL)
    public ResponseEntity deleteArticle(
            @PathVariable(ID_VARIABLE) Long id
    ) {
        ArticleDTO article = articleService.deleteArticle(id);
        if (article == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    private ArticleDTO setAuthorAndAdd(Long authorId, ArticleDTO articleDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(authorId);
        articleDTO.setAuthor(userDTO);
        return articleService.add(articleDTO);
    }
}
