package ru.mail.krivonos.al.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.mail.krivonos.al.controller.validator.UserValidator;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserValidator userValidator;
    @Mock
    private RoleService roleService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private Model model;

    private UserController userController;

    @Before
    public void init() {
        userController = new UserController(userService, userValidator, roleService);
    }

    @Test
    public void shouldReturnRedirectToUsersFirstPageForUsersGetRequest() {
        String users = userController.getUsersPage();
        assertEquals("redirect:/private/users/1", users);
    }

    @Test
    public void shouldReturnAddUserPageForAddGetRequest() {
        String addUserPage = userController.getAddUserPage(model);
        assertEquals("private/add-user", addUserPage);
    }

    @Test
    public void shouldReturnRedirectToUsersPageForAddUserPostRequestWithoutErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = userController.saveUser(new UserDTO(), model, bindingResult);
        assertEquals("redirect:/private/users/1?added", result);
    }

    @Test
    public void shouldReturnAddUserPageForAddPostRequestWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = userController.saveUser(new UserDTO(), model, bindingResult);
        assertEquals("private/add-user", result);
    }

    @Test
    public void shouldReturnRedirectToUsersPageWithPositiveUpdateParamForSuccessfulUpdateRequest() {
        UserDTO userDTO = new UserDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("name");
        userDTO.setRole(roleDTO);
        Long id = 1L;
        when(userService.updateRole(id, roleDTO.getName())).thenReturn(1);
        String result = userController.updateRole(id, userDTO);
        assertEquals("redirect:/private/users/1?updated", result);
    }

    @Test
    public void shouldReturnRedirectToUsersPageWithNegativeUpdateParamForUnsuccessfulUpdateRequest() {
        UserDTO userDTO = new UserDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("name");
        userDTO.setRole(roleDTO);
        Long id = 1L;
        when(userService.updateRole(id, roleDTO.getName())).thenReturn(0);
        String result = userController.updateRole(id, userDTO);
        assertEquals("redirect:/private/users/1?updated_zero", result);
    }

    @Test
    public void shouldReturnRedirectToUsersPageWithPositiveDeleteParamForSuccessfulDeleteRequest() {
        Long[] ids = {1L, 2L};
        when(userService.deleteUsers(ids)).thenReturn(2);
        String result = userController.deleteUsers(ids);
        assertEquals("redirect:/private/users/1?deleted", result);
    }

    @Test
    public void shouldReturnRedirectToUsersPageWithNegativeDeleteParamForUnsuccessfulDeleteRequest() {
        Long[] ids = {1L, 2L};
        when(userService.deleteUsers(ids)).thenReturn(0);
        String result = userController.deleteUsers(ids);
        assertEquals("redirect:/private/users/1?deleted_zero", result);
    }

    @Test
    public void shouldReturnRedirectToUsersPageWithPositivePasswordParamForSuccessfulChangePasswordRequest() {
        Long id = 1L;
        when(userService.changePassword(id)).thenReturn(1);
        String result = userController.changePassword(id);
        assertEquals("redirect:/private/users/1?password_changed", result);
    }

    @Test
    public void shouldReturnRedirectToUsersPageWithNegativePasswordParamForUnsuccessfulChangePasswordRequest() {
        Long id = 1L;
        when(userService.changePassword(id)).thenReturn(0);
        String result = userController.changePassword(id);
        assertEquals("redirect:/private/users/1?password_error", result);
    }
}
