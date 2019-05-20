package ru.mail.krivonos.al.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_ERROR_403_URL;

@RestController("mainApiController")
public class MainApiController {

    @GetMapping(API_ERROR_403_URL)
    public ResponseEntity sendForbiddenStatusCode() {
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}
