package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Comment;
import ru.mail.krivonos.al.service.converter.CommentConverter;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.model.CommentDTO;

@Component("commentConverter")
public class CommentConverterImpl implements CommentConverter {

    private final UserConverter userConverter;

    @Autowired
    public CommentConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setDateOfCreation(commentDTO.getDateOfCreation());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUser(userConverter.toDTO(comment.getUser()));
        return commentDTO;
    }

    @Override
    public Comment fromDTO(CommentDTO commentDTO) {
        return null;
    }
}
