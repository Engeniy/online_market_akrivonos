package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.model.UserDTO;

import static ru.mail.krivonos.al.controller.constant.FieldConstants.NAME_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.SURNAME_FIELD;

@Component("userUpdatingValidator")
public class UserUpdatingValidator implements Validator {

    private static final String INITIALS_SYMBOLS_VALIDATION_REGEX = "^[A-Za-z]+$";
    private static final int NAME_MAX_LENGTH = 20;
    private static final int SURNAME_MAX_LENGTH = 40;

    private final ProfileValidator profileValidator;

    @Autowired
    public UserUpdatingValidator(ProfileValidator profileValidator) {
        this.profileValidator = profileValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, NAME_FIELD, "user.name.empty");
        ValidationUtils.rejectIfEmpty(errors, SURNAME_FIELD, "user.surname.empty");
        UserDTO userDTO = (UserDTO) o;
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
        profileValidator.validate(userDTO.getProfile(), errors);
    }
}
