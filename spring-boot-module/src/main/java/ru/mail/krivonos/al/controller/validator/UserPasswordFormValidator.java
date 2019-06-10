package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.controller.model.UserPasswordForm;
import ru.mail.krivonos.al.service.PasswordService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.UserDTO;

import static ru.mail.krivonos.al.controller.constant.FieldConstants.NEW_PASSWORD_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.OLD_PASSWORD_FIELD;

@Component("userPasswordFormValidator")
public class UserPasswordFormValidator implements Validator {

    private static final int PASSWORD_MIN_LENGTH = 4;
    private static final int PASSWORD_MAX_LENGTH = 20;

    private final UserService userService;
    private final PasswordService passwordService;

    @Autowired
    public UserPasswordFormValidator(
            UserService userService,
            PasswordService passwordService
    ) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserPasswordForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, OLD_PASSWORD_FIELD, "user.password.empty");
        ValidationUtils.rejectIfEmpty(errors, NEW_PASSWORD_FIELD, "user.password.empty");
        UserPasswordForm passwordForm = (UserPasswordForm) o;
        if (passwordForm.getOldPassword() != null && passwordForm.getOldPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue(OLD_PASSWORD_FIELD, "user.password.length");
        }
        if (passwordForm.getOldPassword() != null && passwordForm.getOldPassword().length() > PASSWORD_MAX_LENGTH) {
            errors.rejectValue(OLD_PASSWORD_FIELD, "user.password.length");
        }
        if (passwordForm.getNewPassword() != null && passwordForm.getNewPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue(NEW_PASSWORD_FIELD, "user.password.length");
        }
        if (passwordForm.getNewPassword() != null && passwordForm.getNewPassword().length() > PASSWORD_MAX_LENGTH) {
            errors.rejectValue(NEW_PASSWORD_FIELD, "user.password.length");
        }
        UserDTO user = userService.getUserByID(passwordForm.getUserId());
        boolean matches = passwordService.matches(passwordForm.getOldPassword(), user.getPassword());
        if (!matches) {
            errors.rejectValue(OLD_PASSWORD_FIELD, "user.old-password.invalid");
        }
    }
}
