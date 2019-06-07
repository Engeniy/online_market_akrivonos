package ru.mail.krivonos.al.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.model.RoleDTO;

import java.util.List;

import static ru.mail.krivonos.al.controller.constant.FieldConstants.NAME_FIELD;
import static ru.mail.krivonos.al.controller.constant.FieldConstants.ROLE_FIELD;

@Component("roleValidator")
public class RoleValidator implements Validator {

    private final RoleService roleService;

    @Autowired
    public RoleValidator(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RoleDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, NAME_FIELD, "user.role.name.empty");
        RoleDTO roleDTO = (RoleDTO) o;
        List<RoleDTO> roles = roleService.getRoles();
        if (roleDTO == null) {
            errors.rejectValue(ROLE_FIELD, "user.role.empty");
        } else {
            boolean matches = false;
            for (RoleDTO role : roles) {
                if (roleDTO.getId().equals(role.getId())) {
                    matches = true;
                }
            }
            if (!matches) {
                errors.rejectValue(ROLE_FIELD, "user.role.invalid");
            }
        }
    }
}
