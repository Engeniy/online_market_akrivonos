package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.List;

public interface ArticleService {

    PageDTO<ArticleDTO> getArticles(int pageNumber);

    List<ArticleDTO> getArticles(int limit, int offset);

    ArticleDTO getArticle(Long articleID);

    ArticleDTO add(ArticleDTO articleDTO);

    void deleteArticle(Long articleId);
}
