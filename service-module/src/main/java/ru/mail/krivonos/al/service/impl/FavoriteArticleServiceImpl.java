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
        int countOfEntities = favoriteArticleRepository.getCountOfEntities();
        PageDTO<FavoriteArticleDTO> pageDTO = new PageDTO<>();
        int offset = getOffsetAndSetPages(pageDTO, pageNumber, countOfEntities);
        List<FavoriteArticle> allByUserId = favoriteArticleRepository
                .findAllByUserId(ARTICLES_LIMIT, offset, userId);
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

    private int getOffsetAndSetPages(PageDTO<FavoriteArticleDTO> pageDTO, int pageNumber, int countOfEntities) {
        int countOfPages = pageCountingService.getCountOfPages(countOfEntities, ARTICLES_LIMIT);
        pageDTO.setCountOfPages(countOfPages);
        int currentPageNumber = pageCountingService.getCurrentPageNumber(pageNumber, countOfPages);
        pageDTO.setCurrentPageNumber(currentPageNumber);
        return pageCountingService.getOffset(currentPageNumber, ARTICLES_LIMIT);
    }
}
