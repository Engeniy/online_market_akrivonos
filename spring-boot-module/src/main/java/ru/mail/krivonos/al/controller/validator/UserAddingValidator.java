package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.UserDTO;

import static ru.mail.krivonos.al.controller.constant.FieldConstants.EMAIL_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.NAME_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.SURNAME_FIELD;

@Component("userAddingValidator")
public class UserAddingValidator implements Validator {

    private static final String EMAIL_VALIDATION_REGEX =
            "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
    private static final String INITIALS_SYMBOLS_VALIDATION_REGEX = "^[A-Za-z]+$";
    private static final int EMAIL_MAX_LENGTH = 50;
    private static final int NAME_MAX_LENGTH = 20;
    private static final int SURNAME_MAX_LENGTH = 40;

    private final UserService userService;
    private final RoleValidator roleValidator;

    @Autowired
    public UserAddingValidator(
            UserService userService,
            RoleValidator roleValidator
    ) {
        this.userService = userService;
        this.roleValidator = roleValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, EMAIL_FIELD, "user.email.empty");
        ValidationUtils.rejectIfEmpty(errors, NAME_FIELD, "user.name.empty");
        ValidationUtils.rejectIfEmpty(errors, SURNAME_FIELD, "user.surname.empty");
        UserDTO userDTO = (UserDTO) o;
        UserDTO userByEmail = userService.getUserByEmail(userDTO.getEmail());
        if (userByEmail != null) {
            errors.reject(EMAIL_FIELD, "user.email.exist");
        }
        if (userDTO.getEmail() != null && userDTO.getEmail().length() > EMAIL_MAX_LENGTH) {
            errors.rejectValue(EMAIL_FIELD, "user.email.length");
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().toLowerCase().matches(EMAIL_VALIDATION_REGEX)) {
            errors.rejectValue(EMAIL_FIELD, "user.email.invalid");
        }
        if (userDTO.getName() != null && userDTO.getName().length() > NAME_MAX_LENGTH) {
            errors.rejectValue(NAME_FIELD, "user.name.length");
        }
        if (userDTO.getName() != null && !userDTO.getName().matches(INITIALS_SYMBOLS_VALIDATION_REGEX)) {
            errors.rejectValue(NAME_FIELD, "user.name.symbols");
        }
        if (userDTO.getSurname() != null && userDTO.getSurname().length() > SURNAME_MAX_LENGTH) {
            errors.rejectValue(SURNAME_FIELD, "user.surname.length");
        }
        if (userDTO.getSurname() != null && !userDTO.getSurname().matches(INITIALS_SYMBOLS_VALIDATION_REGEX)) {
            errors.rejectValue(SURNAME_FIELD, "user.surname.symbols");
        }
        roleValidator.validate(userDTO.getRole(), errors);
    }
}
