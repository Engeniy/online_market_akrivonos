package ru.mail.krivonos.al.controller.config.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mail.krivonos.al.controller.ArticleApiController;
import ru.mail.krivonos.al.controller.ItemApiController;
import ru.mail.krivonos.al.controller.MainApiController;
import ru.mail.krivonos.al.controller.OrderApiController;
import ru.mail.krivonos.al.controller.RoleApiController;
import ru.mail.krivonos.al.controller.UserApiController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(assignableTypes = {
        ArticleApiController.class, ItemApiController.class, MainApiController.class,
        OrderApiController.class, RoleApiController.class, UserApiController.class
})
public class ApiExceptionHandler {

    private static final String URL_GET_ERROR_MESSAGE = "Error while getting url: \"{}\".";

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity defaultErrorHandler(HttpServletRequest request, Exception e) {
        logger.error(URL_GET_ERROR_MESSAGE, request.getRequestURL());
        logger.error(e.getMessage(), e);
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
