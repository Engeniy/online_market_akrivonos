package ru.mail.krivonos.al.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.exceptions.UserServiceException;
import ru.mail.krivonos.al.service.impl.UserServiceImpl;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private Connection connection;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordService passwordService;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PageCountingService pageCountingService;

    private UserService userService;

    @Before
    public void init() {
        userService = new UserServiceImpl(userRepository, userConverter, passwordService, pageCountingService);
        when(userRepository.getConnection()).thenReturn(connection);
    }

    @Test
    public void shouldGetEmailAndReturnUserDTOForGetUserByEmailMethodCall() {
        String email = "email";
        User user = new User();
        UserDTO userDTO = new UserDTO();
        when(userRepository.findUserByEmail(connection, email)).thenReturn(user);
        when(userConverter.toDTO(user)).thenReturn(userDTO);
        UserDTO userByEmail = userService.getUserByEmail(email);
        assertEquals(userDTO, userByEmail);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForGetUserByEmailMethodCall() {
        String email = "email";
        when(userRepository.findUserByEmail(connection, email)).thenThrow(new RuntimeException());
        userService.getUserByEmail(email);
    }

    @Test
    public void shouldGetPageNumberAndReturnUserDTOListForGetUsersMethodCall() {
        Integer pageNumber = 1;
        User user = new User();
        UserDTO userDTO = new UserDTO();
        List<User> users = Collections.singletonList(user);
        when(userRepository.findUsers(connection, pageNumber)).thenReturn(users);
        when(userConverter.toDTO(user)).thenReturn(userDTO);
        List<UserDTO> returnedUsers = userService.getUsers(pageNumber);
        assertEquals(userDTO, returnedUsers.get(0));
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForGetUsers() {
        Integer pageNumber = 1;
        when(userRepository.findUsers(connection, pageNumber)).thenThrow(new RuntimeException());
        userService.getUsers(pageNumber);
    }

    @Test
    public void shouldReturnUpdatedNumberForUpdateRoleMethodCall() {
        Long id = 1L;
        int updated = 1;
        String roleName = "Admin";
        when(userRepository.updateRole(connection, id, roleName)).thenReturn(updated);
        int result = userService.updateRole(id, roleName);
        assertEquals(updated, result);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForUpdateRoleMethodCall() {
        Long id = 1L;
        String roleName = "Admin";
        when(userRepository.updateRole(connection, id, roleName)).thenThrow(new RuntimeException());
        userService.updateRole(id, roleName);
    }

    @Test
    public void shouldExecuteMethodAddUserWithoutExceptions() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userConverter.fromDTO(userDTO)).thenReturn(user);
        when(passwordService.getPassword()).thenReturn("password");
        userService.add(userDTO);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromPasswordServiceForAddMethodCall() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userConverter.fromDTO(userDTO)).thenReturn(user);
        when(passwordService.getPassword()).thenThrow(new RuntimeException());
        userService.add(userDTO);
    }

    @Test
    public void shouldReturnDeletedNumberForDeleteUsersMethodCall() {
        Long[] ids = {1L};
        int deleted = 1;
        when(userRepository.deleteUsers(connection, ids)).thenReturn(deleted);
        int result = userService.deleteUsers(ids);
        assertEquals(deleted, result);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForDeleteUsersMethodCall() {
        Long[] ids = {1L};
        when(userRepository.deleteUsers(connection, ids)).thenThrow(new RuntimeException());
        userService.deleteUsers(ids);
    }

    @Test
    public void shouldReturnChangedNumberForChangePasswordMethodCall() {
        Long id = 1L;
        String password = "password";
        int changed = 1;
        when(passwordService.getPassword()).thenReturn(password);
        when(userRepository.findUserByID(connection, id)).thenReturn(new User());
        when(userRepository.changePassword(connection, id, password)).thenReturn(changed);
        int result = userService.changePassword(id);
        assertEquals(changed, result);
    }

    @Test
    public void shouldReturnZeroIfUserWithSuchIdDoesntExistForChangePasswordMethodCall() {
        Long id = 1L;
        int changed = 0;
        when(userRepository.findUserByID(connection, id)).thenReturn(null);
        int result = userService.changePassword(id);
        assertEquals(changed, result);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryForChangePasswordMethodCall() {
        Long id = 1L;
        int changed = 0;
        when(userRepository.findUserByID(connection, id)).thenThrow(new RuntimeException());
        int result = userService.changePassword(id);
        assertEquals(changed, result);
    }

    @Test
    public void shouldReturnPagesNumberFromGetPagesNumberMethod() {
        int usersNumber = 20;
        int limit = 10;
        int pagesNumber = 2;
        when(userRepository.countUsers(connection)).thenReturn(usersNumber);
        when(pageCountingService.countPages(usersNumber, limit)).thenReturn(pagesNumber);
        int result = userService.getPagesNumber();
        assertEquals(pagesNumber, result);
    }

    @Test(expected = UserServiceException.class)
    public void shouldThrowUserServiceExceptionWhenCatchingExceptionFromRepositoryFromGetPagesNumberMethod() {
        int usersNumber = 20;
        int limit = 10;
        when(userRepository.countUsers(connection)).thenReturn(usersNumber);
        when(pageCountingService.countPages(usersNumber, limit)).thenThrow(new RuntimeException());
        userService.getPagesNumber();
    }
}
