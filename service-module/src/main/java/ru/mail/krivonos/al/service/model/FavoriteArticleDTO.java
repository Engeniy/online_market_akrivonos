package ru.mail.krivonos.al.service.model;

import ru.mail.krivonos.al.repository.model.FavoriteArticleKey;

public class FavoriteArticleDTO {

    private FavoriteArticleKey id;
    private UserDTO user;
    private ArticleDTO article;

    public FavoriteArticleKey getId() {
        return id;
    }

    public void setId(FavoriteArticleKey id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }
}
