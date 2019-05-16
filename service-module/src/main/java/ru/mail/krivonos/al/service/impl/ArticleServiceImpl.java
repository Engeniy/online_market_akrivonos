package ru.mail.krivonos.al.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticles(int pageNumber) {
        return null;
    }
}
