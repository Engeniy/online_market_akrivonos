package ru.mail.krivonos.al.controller.config.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static ru.mail.krivonos.al.controller.constant.URLConstants.INTERNAL_ERROR_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_TEMPLATE;

@ControllerAdvice
public class AppExceptionHandler {

    private static final String URL_GET_ERROR_MESSAGE = "Error while getting url: \"{}\".";

    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String defaultErrorHandler(HttpServletRequest request, Exception e) {
        logger.error(URL_GET_ERROR_MESSAGE, request.getRequestURL());
        logger.error(e.getMessage(), e);
        return String.format(REDIRECT_TEMPLATE, INTERNAL_ERROR_PAGE_URL);
    }
}
