package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.ArticleRepository;
import ru.mail.krivonos.al.repository.CommentRepository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.Article;
import ru.mail.krivonos.al.repository.model.Comment;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.CommentService;
import ru.mail.krivonos.al.service.model.CommentDTO;

import java.util.Date;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            CommentRepository commentRepository
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void addComment(Long articleId, CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setDateOfCreation(new Date());
        User author = userRepository.findById(commentDTO.getAuthor().getId());
        comment.setAuthor(author);
        Article article = articleRepository.findById(articleId);
        comment.setArticle(article);
        commentRepository.persist(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId);
        commentRepository.remove(comment);
    }
}
