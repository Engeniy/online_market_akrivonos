package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.ArticleDTO;
import ru.mail.krivonos.al.service.model.PageDTO;

public interface ArticleService {

    PageDTO<ArticleDTO> getArticles(int pageNumber);
}
