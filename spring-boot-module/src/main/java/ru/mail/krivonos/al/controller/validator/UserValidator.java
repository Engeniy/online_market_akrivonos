package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.UserDTO;

@Component("userValidator")
public class UserValidator implements Validator {

    private static final String EMAIL_VALIDATION_REGEX =
            "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    private static final String INITIALS_SYMBOLS_VALIDATION_REGEX = "^[A-Za-z]+$";
    private static final int EMAIL_MAX_LENGTH = 50;
    private static final int NAME_MAX_LENGTH = 20;
    private static final int SURNAME_MAX_LENGTH = 40;
    private static final int PATRONYMIC_MAX_LENGTH = 40;

    private final UserService userService;
    private final RoleValidator roleValidator;

    @Autowired
    public UserValidator(UserService userService, RoleValidator roleValidator) {
        this.userService = userService;
        this.roleValidator = roleValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "email", "user.email.empty");
        ValidationUtils.rejectIfEmpty(errors, "name", "user.name.empty");
        ValidationUtils.rejectIfEmpty(errors, "surname", "user.surname.empty");
        UserDTO userDTO = (UserDTO) o;
        UserDTO userByEmail = userService.getUserByEmail(userDTO.getEmail());
        if (userByEmail != null) {
            errors.reject("email", "user.email.exist");
        }
        if (userDTO.getEmail() != null && userDTO.getEmail().length() > EMAIL_MAX_LENGTH) {
            errors.rejectValue("email", "user.email.length");
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().toLowerCase().matches(EMAIL_VALIDATION_REGEX)) {
            errors.rejectValue("email", "user.email.invalid", "Invalid email form");
        }
        if (userDTO.getName() != null && userDTO.getName().length() > NAME_MAX_LENGTH) {
            errors.rejectValue("name", "user.name.length");
        }
        if (userDTO.getName() != null && !userDTO.getName().matches(INITIALS_SYMBOLS_VALIDATION_REGEX)) {
            errors.rejectValue("name", "user.name.symbols", "Invalid name form");
        }
        if (userDTO.getSurname() != null && userDTO.getSurname().length() > SURNAME_MAX_LENGTH) {
            errors.rejectValue("surname", "user.surname.length");
        }
        if (userDTO.getSurname() != null && !userDTO.getSurname().matches(INITIALS_SYMBOLS_VALIDATION_REGEX)) {
            errors.rejectValue("surname", "user.surname.symbols");
        }
        if (userDTO.getPatronymic() != null && !userDTO.getPatronymic().isEmpty() &&
                userDTO.getPatronymic().length() > PATRONYMIC_MAX_LENGTH) {
            errors.rejectValue("patronymic", "user.patronymic.length");
        }
        if (userDTO.getPatronymic() != null && !userDTO.getPatronymic().isEmpty() &&
                !userDTO.getPatronymic().matches(INITIALS_SYMBOLS_VALIDATION_REGEX)) {
            errors.rejectValue("patronymic", "user.patronymic.symbols");
        }
        roleValidator.validate(userDTO.getRole(), errors);
    }
}
