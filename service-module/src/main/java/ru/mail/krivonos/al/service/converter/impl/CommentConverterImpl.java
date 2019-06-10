package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.Comment;
import ru.mail.krivonos.al.service.converter.CommentConverter;
import ru.mail.krivonos.al.service.converter.UserConverterAggregator;
import ru.mail.krivonos.al.service.model.CommentDTO;

@Component("commentConverter")
public class CommentConverterImpl implements CommentConverter {

    private final UserConverterAggregator userConverterAggregator;

    @Autowired
    public CommentConverterImpl(UserConverterAggregator userConverterAggregator) {
        this.userConverterAggregator = userConverterAggregator;
    }

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setDateOfCreation(comment.getDateOfCreation());
        commentDTO.setContent(comment.getContent());
        commentDTO.setAuthor(userConverterAggregator.getAuthorConverter().toDTO(comment.getAuthor()));
        return commentDTO;
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        throw new UnsupportedOperationException("toEntity() method in unsupported for CommentConverterImpl class.");
    }
}
