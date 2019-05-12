package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mail.krivonos.al.controller.validator.UserValidator;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.mail.krivonos.al.controller.constant.PageConstants.ADD_USER_PAGE;
import static ru.mail.krivonos.al.controller.constant.PageConstants.USERS_PAGE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.REDIRECT_WITH_PARAMETER_TEMPLATE;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_ADD_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_DELETE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_FIRST_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PAGE_WITH_PAGE_NUMBER_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PASSWORD_CHANGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_UPDATE_URL;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.ADDED_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_NEGATIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.DELETED_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PASSWORD_CHANGE_NEGATIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.PASSWORD_CHANGE_POSITIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPDATED_NEGATIVE_PARAM;
import static ru.mail.krivonos.al.controller.constant.URLParametersConstants.UPDATED_POSITIVE_PARAM;

@Controller("userController")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }

    @GetMapping(USERS_PAGE_URL)
    public String getUsersPage() {
        return String.format(REDIRECT_TEMPLATE, USERS_FIRST_PAGE_URL);
    }

    @GetMapping(USERS_PAGE_WITH_PAGE_NUMBER_URL)
    public String getUsersWithPageNumber(
            @PathVariable("page") Integer page, Model model
    ) {
        Optional<Integer> pageOptional = Optional.ofNullable(page);
        int pageNumber = pageOptional.orElse(1);
        int pagesNumber = userService.getPagesNumber();
        if (pageNumber > pagesNumber && pagesNumber > 0) {
            pageNumber = pagesNumber;
        }
        List<Integer> pagesNumbers = IntStream
                .rangeClosed(1, pagesNumber)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pages", pagesNumbers);
        model.addAttribute("current_page", pageNumber);
        List<UserDTO> users = userService.getUsers(pageNumber);
        model.addAttribute("users", users);
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
        model.addAttribute("user", user);
        List<RoleDTO> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        return ADD_USER_PAGE;
    }

    @PostMapping(USERS_ADD_PAGE_URL)
    public String saveUser(
            @ModelAttribute("user") UserDTO user, Model model, BindingResult result
    ) {
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            List<RoleDTO> roles = roleService.getRoles();
            model.addAttribute("roles", roles);
            return ADD_USER_PAGE;
        }
        userService.add(user);
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, ADDED_PARAM);
    }

    @PostMapping(USERS_UPDATE_URL)
    public String updateRole(
            @PathVariable("id") Long id,
            @ModelAttribute("user") UserDTO user
    ) {
        int updated = userService.updateRole(id, user.getRole().getName());
        if (updated == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, UPDATED_NEGATIVE_PARAM);
        }
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, UPDATED_POSITIVE_PARAM);
    }

    @PostMapping(USERS_DELETE_URL)
    public String deleteUsers(
            @RequestParam("user_ids") Long[] usersIDs
    ) {
        if (usersIDs.length == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, DELETED_NEGATIVE_PARAM);
        }
        int deleted = userService.deleteUsers(usersIDs);
        if (deleted == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, DELETED_NEGATIVE_PARAM);
        }
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, DELETED_POSITIVE_PARAM);
    }

    @PostMapping(USERS_PASSWORD_CHANGE_URL)
    public String changePassword(
            @PathVariable("id") Long id
    ) {
        int changed = userService.changePassword(id);
        if (changed == 0) {
            return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, PASSWORD_CHANGE_NEGATIVE_PARAM);
        }
        return String.format(REDIRECT_WITH_PARAMETER_TEMPLATE, USERS_FIRST_PAGE_URL, PASSWORD_CHANGE_POSITIVE_PARAM);
    }
}
