package ru.mail.krivonos.al.service.model;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {

    private int currentPageNumber;
    private int countOfPages;
    private List<T> list = new ArrayList<>();

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getCountOfPages() {
        return countOfPages;
    }

    public void setCountOfPages(int countOfPages) {
        this.countOfPages = countOfPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
