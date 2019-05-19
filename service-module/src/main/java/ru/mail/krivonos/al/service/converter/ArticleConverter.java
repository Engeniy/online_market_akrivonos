package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.Article;
import ru.mail.krivonos.al.service.model.ArticleDTO;

public interface ArticleConverter {

    ArticleDTO toDTO(Article article);

    Article toEntity(ArticleDTO articleDTO);
}
