package ru.mail.krivonos.al.service.impl;

import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.service.PageCountingService;

@Service("pageCountingService")
public class PageCountingServiceImpl implements PageCountingService {

    @Override
    public int countPages(int itemsNumber, int pageLimit) {
        int pagesNumber = itemsNumber / pageLimit;
        if (itemsNumber > (pagesNumber * pageLimit)) {
            pagesNumber += 1;
        }
        return pagesNumber;
    }
}
