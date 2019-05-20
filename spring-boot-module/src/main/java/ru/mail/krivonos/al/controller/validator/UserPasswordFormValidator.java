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
        ValidationUtils.rejectIfEmpty(errors, "oldPassword", "user.password.empty");
        ValidationUtils.rejectIfEmpty(errors, "newPassword", "user.password.empty");
        UserPasswordForm passwordForm = (UserPasswordForm) o;
        if (passwordForm.getOldPassword() != null && passwordForm.getOldPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue("oldPassword", "user.password.length");
        }
        if (passwordForm.getOldPassword() != null && passwordForm.getOldPassword().length() > PASSWORD_MAX_LENGTH) {
            errors.rejectValue("oldPassword", "user.password.length");
        }
        if (passwordForm.getNewPassword() != null && passwordForm.getNewPassword().length() < PASSWORD_MIN_LENGTH) {
            errors.rejectValue("newPassword", "user.password.length");
        }
        if (passwordForm.getNewPassword() != null && passwordForm.getNewPassword().length() > PASSWORD_MAX_LENGTH) {
            errors.rejectValue("newPassword", "user.password.length");
        }
        UserDTO userByID = userService.getUserByID(passwordForm.getUserId());
        boolean matches = passwordService.matches(passwordForm.getOldPassword(), userByID.getPassword());
        if (!matches) {
            errors.rejectValue("oldPassword", "user.old-password.invalid");
        }
    }
}
