package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.Article;

public interface ArticleRepository extends GenericRepository<Long, Article> {

    int getCountOfEntities();
}
