package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Article;
import ru.mail.krivonos.al.repository.model.Comment;
import ru.mail.krivonos.al.service.converter.ArticleConverter;
import ru.mail.krivonos.al.service.converter.CommentConverter;
import ru.mail.krivonos.al.service.converter.UserConverterAggregator;
import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.CommentDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component("articleConverter")
public class ArticleConverterImpl implements ArticleConverter {

    private final CommentConverter commentConverter;
    private final UserConverterAggregator userConverterAggregator;

    @Autowired
    public ArticleConverterImpl(
            CommentConverter commentConverter,
            UserConverterAggregator userConverterAggregator
    ) {
        this.commentConverter = commentConverter;
        this.userConverterAggregator = userConverterAggregator;
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setDateOfCreation(article.getDateOfCreation());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setAuthor(userConverterAggregator.getAuthorConverter().toDTO(article.getAuthor()));
        articleDTO.setSummary(article.getSummary());
        articleDTO.setContent(article.getContent());
        articleDTO.setComments(getCommentDTOs(article.getComments()));
        return articleDTO;
    }

    @Override
    public Article toEntity(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setContent(articleDTO.getContent());
        article.setTitle(articleDTO.getTitle());
        article.setDateOfCreation(articleDTO.getDateOfCreation());
        article.setAuthor(userConverterAggregator.getAuthorConverter().toEntity(articleDTO.getAuthor()));
        return article;
    }

    private List<CommentDTO> getCommentDTOs(List<Comment> comments) {
        return comments.stream()
                .map(commentConverter::toDTO)
                .collect(Collectors.toList());
    }
}
