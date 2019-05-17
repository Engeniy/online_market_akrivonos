package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ArticleRepository;
import ru.mail.krivonos.al.repository.model.Article;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.converter.ArticleConverter;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.ARTICLES_LIMIT;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final PageCountingService pageCountingService;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            ArticleConverter articleConverter,
            PageCountingService pageCountingService
    ) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
        this.pageCountingService = pageCountingService;
    }

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticles(int pageNumber) {
        PageDTO<ArticleDTO> pageDTO = new PageDTO();
        int countOfPages = articleRepository.getCountOfPages();
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, ARTICLES_LIMIT);
        List<Article> articles = articleRepository.findAll(ARTICLES_LIMIT, offset);
        List<ArticleDTO> articleDTOs = getArticleDTOs(articles);
        pageDTO.setList(articleDTOs);
        return pageDTO;
    }

    private List<ArticleDTO> getArticleDTOs(List<Article> articles) {
        return articles.stream()
                .map(articleConverter::toDTO)
                .collect(Collectors.toList());
    }
}
