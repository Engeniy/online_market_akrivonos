package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.FavoriteArticle;
import ru.mail.krivonos.al.repository.model.FavoriteArticleKey;

import java.util.List;

public interface FavoriteArticleRepository extends GenericRepository<FavoriteArticleKey, FavoriteArticle> {

    List<FavoriteArticle> findAllByUserId(int limit, int offset, Long userId);
}
