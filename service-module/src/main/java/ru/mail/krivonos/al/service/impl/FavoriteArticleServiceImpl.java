package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ArticleRepository;
import ru.mail.krivonos.al.repository.FavoriteArticleRepository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.Article;
import ru.mail.krivonos.al.repository.model.FavoriteArticle;
import ru.mail.krivonos.al.repository.model.FavoriteArticleKey;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.FavoriteArticleService;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.converter.FavoriteArticleConverter;
import ru.mail.krivonos.al.service.model.FavoriteArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.LimitConstants.ARTICLES_LIMIT;
import static ru.mail.krivonos.al.service.constant.OrderConstants.DATE_OF_CREATION;

@Service("favoriteArticleService")
public class FavoriteArticleServiceImpl implements FavoriteArticleService {

    private final FavoriteArticleRepository favoriteArticleRepository;
    private final FavoriteArticleConverter favoriteArticleConverter;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final PageCountingService pageCountingService;

    @Autowired
    public FavoriteArticleServiceImpl(
            FavoriteArticleRepository favoriteArticleRepository,
            FavoriteArticleConverter favoriteArticleConverter,
            UserRepository userRepository,
            ArticleRepository articleRepository,
            PageCountingService pageCountingService
    ) {
        this.favoriteArticleRepository = favoriteArticleRepository;
        this.favoriteArticleConverter = favoriteArticleConverter;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.pageCountingService = pageCountingService;
    }

    @Override
    @Transactional
    public PageDTO<FavoriteArticleDTO> getArticlesByUserId(int pageNumber, Long userId) {
        PageDTO<FavoriteArticleDTO> pageDTO = new PageDTO<>();
        int countOfEntities = favoriteArticleRepository.getCountOfEntities();
        int countOfPages = pageCountingService.countPages(countOfEntities, ARTICLES_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        int offset = pageCountingService.getOffset(currentPageNumber, ARTICLES_LIMIT);
        List<FavoriteArticle> allByUserId = favoriteArticleRepository
                .findAllByUserIdWithDescOrder(ARTICLES_LIMIT, offset, userId, DATE_OF_CREATION);
        pageDTO.setList(getFavoriteArticleDTOs(allByUserId));
        return pageDTO;
    }

    @Override
    @Transactional
    public void addFavoriteArticle(FavoriteArticleKey favoriteArticleKey) {
        FavoriteArticle byId = favoriteArticleRepository.findById(favoriteArticleKey);
        if (byId == null) {
            FavoriteArticle favoriteArticle = new FavoriteArticle();
            favoriteArticle.setId(favoriteArticleKey);
            User user = userRepository.findById(favoriteArticleKey.getUserId());
            favoriteArticle.setUser(user);
            Article article = articleRepository.findById(favoriteArticleKey.getArticleId());
            favoriteArticle.setArticle(article);
            favoriteArticleRepository.persist(favoriteArticle);
        }
    }

    private List<FavoriteArticleDTO> getFavoriteArticleDTOs(List<FavoriteArticle> favoriteArticles) {
        return favoriteArticles.stream()
                .map(favoriteArticleConverter::toDTO)
                .collect(Collectors.toList());
    }
}
