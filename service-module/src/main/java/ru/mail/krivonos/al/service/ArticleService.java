package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.PageDTO;

public interface ArticleService {

    PageDTO getArticles(int pageNumber);
}
