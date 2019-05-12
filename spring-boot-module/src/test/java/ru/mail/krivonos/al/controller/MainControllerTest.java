package ru.mail.krivonos.al.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainControllerTest {

    private MainController mainController = new MainController();

    @Test
    public void shouldReturnLoginPageForLoginGetRequest() {
        String result = mainController.getLoginPage();
        assertEquals("login", result);
    }

    @Test
    public void shouldReturnForbiddenPageForForbiddenGetRequest() {
        String result = mainController.getForbiddenPage();
        assertEquals("error/403", result);
    }

    @Test
    public void shouldReturnInternalErrorPageForInternalErrorGetRequest() {
        String result = mainController.getInternalErrorPage();
        assertEquals("error/500", result);
    }
}
