package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.ArticleRepository;
import ru.mail.krivonos.al.repository.model.Article;

@Repository("articleRepository")
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
}
