package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.Comment;
import ru.mail.krivonos.al.service.model.CommentDTO;

public interface CommentConverter {

    CommentDTO toDTO(Comment comment);

    Comment toEntity(CommentDTO commentDTO);
}
