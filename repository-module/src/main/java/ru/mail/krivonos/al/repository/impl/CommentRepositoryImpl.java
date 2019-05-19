package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.CommentRepository;
import ru.mail.krivonos.al.repository.model.Comment;

@Repository("commentRepository")
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
}
