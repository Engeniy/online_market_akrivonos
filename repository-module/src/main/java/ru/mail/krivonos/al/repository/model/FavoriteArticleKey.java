package ru.mail.krivonos.al.repository.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoriteArticleKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "article_id")
    private Long articleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteArticleKey)) return false;
        FavoriteArticleKey that = (FavoriteArticleKey) o;
        return userId.equals(that.userId) &&
                articleId.equals(that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, articleId);
    }
}
