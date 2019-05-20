package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.FavoriteArticle;
import ru.mail.krivonos.al.service.model.FavoriteArticleDTO;

public interface FavoriteArticleConverter {

    FavoriteArticleDTO toDTO(FavoriteArticle favoriteArticle);
}
