package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.krivonos.al.controller.validator.UserValidatorAggregator;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.UserDTO;

import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_USERS_URL;

@RestController("userApiController")
public class UserApiController {

    private final UserService userService;
    private final UserValidatorAggregator userValidatorAggregator;

    @Autowired
    public UserApiController(
            UserService userService,
            UserValidatorAggregator userValidatorAggregator
    ) {
        this.userService = userService;
        this.userValidatorAggregator = userValidatorAggregator;
    }

    @PostMapping(API_USERS_URL)
    public ResponseEntity saveUser(
            @RequestBody UserDTO userDTO, BindingResult bindingResult
    ) {
        userValidatorAggregator.getUserAddingValidator().validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.add(userDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
