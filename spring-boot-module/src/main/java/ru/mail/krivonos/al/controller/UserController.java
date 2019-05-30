package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.controller.model.UserPasswordForm;
import ru.mail.krivonos.al.controller.validator.UserPasswordFormValidator;
import ru.mail.krivonos.al.controller.validator.UserValidatorAggregator;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.ProfileDTO;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.util.List;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ADD_USER_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.PROFILE_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.USERS_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.PROFILE_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.PROFILE_PASSWORD_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.PROFILE_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_ADD_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PASSWORD_CHANGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ADDED_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_NEGATIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PAGE_NUMBER_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PASSWORD_CHANGE_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPDATED_POSITIVE_PARAM;

@Controller("userController")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidatorAggregator userValidatorAggregator;
    private final UserPasswordFormValidator passwordFormValidator;

    @Autowired
    public UserController(
            UserService userService,
            RoleService roleService,
            UserValidatorAggregator userValidatorAggregator,
            UserPasswordFormValidator passwordFormValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidatorAggregator = userValidatorAggregator;
        this.passwordFormValidator = passwordFormValidator;
    }

    @GetMapping(USERS_PAGE_URL)
    public String getUsers(
            @RequestParam(name = "page", defaultValue = "1") Integer page, Model model
    ) {
        PageDTO<UserDTO> pageDTO = userService.getUsers(page);
        model.addAttribute("page", pageDTO);
        UserDTO user = new UserDTO();
        user.setRole(new RoleDTO());
        model.addAttribute("user", user);
        List<RoleDTO> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return USERS_PAGE;
    }

    @GetMapping(USERS_ADD_PAGE_URL)
    public String getAddUserPage(Model model) {
        UserDTO user = new UserDTO();
        user.setRole(new RoleDTO());
        user.setProfile(new ProfileDTO());
        model.addAttribute("user", user);
        List<RoleDTO> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return ADD_USER_PAGE;
    }

    @PostMapping(USERS_ADD_PAGE_URL)
    public String saveUser(
            @ModelAttribute("user") UserDTO user, Model model, BindingResult result
    ) {
        userValidatorAggregator.getUserAddingValidator().validate(user, result);
        if (result.hasErrors()) {
            List<RoleDTO> roles = roleService.getRoles();
            model.addAttribute("roles", roles);
            return ADD_USER_PAGE;
        }
        userService.add(user);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_PAGE_URL, ADDED_PARAM);
    }

    @PostMapping(USERS_UPDATE_URL)
    public String updateRole(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber,
            @ModelAttribute("user") UserDTO user
    ) {
        userService.updateRole(id, user.getRole().getId());
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, USERS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, UPDATED_POSITIVE_PARAM);
    }

    @PostMapping(USERS_DELETE_URL)
    public String deleteUsers(
            @RequestParam("user_ids") Long[] usersIDs,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber
    ) {
        if (usersIDs.length == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_PAGE_URL, DELETED_NEGATIVE_PARAM);
        }
        userService.deleteUsers(usersIDs);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, USERS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, DELETED_POSITIVE_PARAM);
    }

    @PostMapping(USERS_PASSWORD_CHANGE_URL)
    public String changePassword(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "1") Integer pageNumber
    ) {
        userService.changePassword(id);
        return String.format(REDIRECT_WITH_TWO_PARAMETERS_TEMPLATE, USERS_PAGE_URL, PAGE_NUMBER_PARAM,
                pageNumber, PASSWORD_CHANGE_POSITIVE_PARAM);
    }

    @GetMapping(PROFILE_PAGE_URL)
    public String getProfilePage(
            @AuthenticationPrincipal AuthUserPrincipal userPrincipal, Model model
    ) {
        UserDTO userDTO = userService.getUserByID(userPrincipal.getUserID());
        model.addAttribute("user", userDTO);
        model.addAttribute("password_form", new UserPasswordForm());
        return PROFILE_PAGE;
    }

    @PostMapping(PROFILE_UPDATE_URL)
    public String updateProfile(
            @PathVariable("id") Long id,
            @ModelAttribute("user") UserDTO userDTO, Model model, BindingResult bindingResult
    ) {
        userValidatorAggregator.getUserUpdatingValidator().validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("password_form", new UserPasswordForm());
            return PROFILE_PAGE;
        }
        userDTO.setId(id);
        userService.updateProfile(userDTO);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, PROFILE_PAGE_URL, UPDATED_POSITIVE_PARAM);
    }

    @PostMapping(PROFILE_PASSWORD_UPDATE_URL)
    public String updatePassword(
            @PathVariable("id") Long id,
            @ModelAttribute("password_form") UserPasswordForm passwordForm, Model model, BindingResult bindingResult
    ) {
        passwordForm.setUserId(id);
        passwordFormValidator.validate(passwordForm, bindingResult);
        if (bindingResult.hasErrors()) {
            UserDTO userDTO = userService.getUserByID(id);
            model.addAttribute("user", userDTO);
            return PROFILE_PAGE;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setPassword(passwordForm.getNewPassword());
        userService.updatePassword(userDTO);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, PROFILE_PAGE_URL, PASSWORD_CHANGE_POSITIVE_PARAM);
    }
}
