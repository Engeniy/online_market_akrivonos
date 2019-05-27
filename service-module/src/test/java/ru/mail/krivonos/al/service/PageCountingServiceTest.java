package ru.mail.krivonos.al.service;

import org.junit.Assert;
import org.junit.Test;
import ru.mail.krivonos.al.service.impl.PageCountingServiceImpl;

public class PageCountingServiceTest {

    private PageCountingService pageCountingService = new PageCountingServiceImpl();

    @Test
    public void shouldReturnTwoPagesForTwentyItemsWithLimitTen() {
        int pagesNumber = pageCountingService.getCountOfPages(20, 10);
        Assert.assertEquals(2, pagesNumber);
    }

    @Test
    public void shouldReturnTreePagesForTwentyOneItemWithLimitTen() {
        int pagesNumber = pageCountingService.getCountOfPages(21, 10);
        Assert.assertEquals(3, pagesNumber);
    }
}
