package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userValidatorAggregator")
public class UserValidatorAggregator {

    private final UserAddingValidator userAddingValidator;
    private final UserUpdatingValidator userUpdatingValidator;

    @Autowired
    public UserValidatorAggregator(
            UserAddingValidator userAddingValidator,
            UserUpdatingValidator userUpdatingValidator
    ) {
        this.userAddingValidator = userAddingValidator;
        this.userUpdatingValidator = userUpdatingValidator;
    }

    public UserAddingValidator getUserAddingValidator() {
        return userAddingValidator;
    }

    public UserUpdatingValidator getUserUpdatingValidator() {
        return userUpdatingValidator;
    }
}
