package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.repository.model.FavoriteArticleKey;
import ru.mail.krivonos.al.service.model.FavoriteArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

public interface FavoriteArticleService {

    PageDTO<FavoriteArticleDTO> getArticlesByUserId(int pageNumber, Long userId);

    void addFavoriteArticle(FavoriteArticleKey favoriteArticleKey);
}
