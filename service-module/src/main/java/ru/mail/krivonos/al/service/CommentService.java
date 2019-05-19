package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.CommentDTO;

public interface CommentService {

    void addComment(Long articleId, CommentDTO commentDTO);

    void deleteComment(Long commentId);
}
