package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ArticleRepository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.Article;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.ArticleService;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.converter.ArticleConverter;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.ARTICLES_LIMIT;
import static ru.mail.krivonos.al.service.constant.LimitConstants.SUMMARY_LENGTH;
import static ru.mail.krivonos.al.service.constant.OrderConstants.DATE_OF_CREATION;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    private static final String SUMMARY_ENDING = "...";

    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final PageCountingService pageCountingService;
    private final UserRepository userRepository;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            ArticleConverter articleConverter,
            PageCountingService pageCountingService,
            UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
        this.pageCountingService = pageCountingService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public PageDTO<ArticleDTO> getArticles(int pageNumber) {
        PageDTO<ArticleDTO> pageDTO = new PageDTO();
        int countOfEntities = articleRepository.getCountOfEntities();
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, ARTICLES_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, ARTICLES_LIMIT);
        List<Article> articles = articleRepository.findAll(ARTICLES_LIMIT, offset);
        List<ArticleDTO> articleDTOs = getArticleDTOs(articles);
        pageDTO.setList(articleDTOs);
        return pageDTO;
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticles(int limit, int offset) {
        List<Article> articles = articleRepository.findAllWithDescendingOrder(limit, offset, DATE_OF_CREATION);
        return getArticleDTOs(articles);
    }

    @Override
    @Transactional
    public ArticleDTO getArticleById(Long articleID) {
        Article article = articleRepository.findById(articleID);
        if (article == null) {
            return null;
        }
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public ArticleDTO add(ArticleDTO articleDTO) {
        Article article = articleConverter.toEntity(articleDTO);
        User author = userRepository.findById(article.getAuthor().getId());
        article.setAuthor(author);
        if (articleDTO.getDateOfCreation() == null) {
            article.setDateOfCreation(new Date());
        } else {
            article.setDateOfCreation(articleDTO.getDateOfCreation());
        }
        article.setSummary(getSummary(article.getContent()));
        articleRepository.persist(article);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public ArticleDTO deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId);
        if (article == null) {
            return null;
        }
        articleRepository.remove(article);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public void update(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getId());
        article.setDateOfCreation(new Date());
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setSummary(getSummary(articleDTO.getContent()));
        articleRepository.merge(article);
    }

    private List<ArticleDTO> getArticleDTOs(List<Article> articles) {
        return articles.stream()
                .map(articleConverter::toDTO)
                .collect(Collectors.toList());
    }

    private String getSummary(String content) {
        if (content.length() > SUMMARY_LENGTH) {
            return content.substring(0, SUMMARY_LENGTH - SUMMARY_ENDING.length()) + SUMMARY_ENDING;
        }
        return content;
    }
}
