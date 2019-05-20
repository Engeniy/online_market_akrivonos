package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.FavoriteArticle;
import ru.mail.krivonos.al.service.converter.ArticleConverter;
import ru.mail.krivonos.al.service.converter.FavoriteArticleConverter;
import ru.mail.krivonos.al.service.model.FavoriteArticleDTO;

@Component("favoriteArticleConverter")
public class FavoriteArticleConverterImpl implements FavoriteArticleConverter {

    private final ArticleConverter articleConverter;

    @Autowired
    public FavoriteArticleConverterImpl(ArticleConverter articleConverter) {
        this.articleConverter = articleConverter;
    }

    @Override
    public FavoriteArticleDTO toDTO(FavoriteArticle favoriteArticle) {
        FavoriteArticleDTO favoriteArticleDTO = new FavoriteArticleDTO();
        favoriteArticleDTO.setId(favoriteArticle.getId());
        favoriteArticleDTO.setArticle(articleConverter.toDTO(favoriteArticle.getArticle()));
        return favoriteArticleDTO;
    }
}
