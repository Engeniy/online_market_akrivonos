package ru.mail.krivonos.al.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static ru.mail.krivonos.al.controller.constant.PageConstants.FORBIDDEN_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.INTERNAL_ERROR_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.LOGIN_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.FORBIDDEN_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.INTERNAL_ERROR_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.LOGIN_PAGE_URL;

@Controller("mainController")
public class MainController {

    @GetMapping(LOGIN_PAGE_URL)
    public String getLoginPage() {
        return LOGIN_PAGE;
    }

    @GetMapping(FORBIDDEN_PAGE_URL)
    public String getForbiddenPage() {
        return FORBIDDEN_PAGE;
    }

    @GetMapping(INTERNAL_ERROR_PAGE_URL)
    public String getInternalErrorPage() {
        return INTERNAL_ERROR_PAGE;
    }
}
