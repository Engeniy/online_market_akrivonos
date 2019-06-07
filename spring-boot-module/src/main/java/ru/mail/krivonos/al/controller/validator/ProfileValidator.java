package ru.mail.krivonos.al.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.model.ProfileDTO;

import static ru.mail.krivonos.al.controller.constant.FieldConstants.ADDRESS_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.TELEPHONE_FIELD;

@Component("profileValidator")
public class ProfileValidator implements Validator {

    private static final int ADDRESS_MAX_LENGTH = 30;
    private static final String PHONE_NUMBER_VALIDATOR = "^\\+\\d{12}$";

    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, ADDRESS_FIELD, "user.profile.address.empty");
        ValidationUtils.rejectIfEmpty(errors, TELEPHONE_FIELD, "user.profile.telephone.empty");
        ProfileDTO profileDTO = (ProfileDTO) o;
        if (profileDTO.getAddress() != null && profileDTO.getAddress().length() > ADDRESS_MAX_LENGTH) {
            errors.rejectValue(ADDRESS_FIELD, "user.profile.address.length");
        }
        if (profileDTO.getTelephone() != null && !profileDTO.getTelephone().matches(PHONE_NUMBER_VALIDATOR)) {
            errors.rejectValue(TELEPHONE_FIELD, "user.profile.telephone.format");
        }
    }
}
