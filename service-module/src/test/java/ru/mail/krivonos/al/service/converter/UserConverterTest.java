package ru.mail.krivonos.al.service.converter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.converter.impl.UserConverterImpl;
import ru.mail.krivonos.al.service.model.UserDTO;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserConverterTest {

    @Mock
    private RoleConverter roleConverter;

    private UserConverter userConverter;

    @Before
    public void init() {
        userConverter = new UserConverterImpl(roleConverter);
    }

    @Test
    public void shouldReturnUserDTOWithSameID() {
        User user = new User();
        user.setId(1L);
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getId(), userDTO.getId());
    }

    @Test
    public void shouldReturnUserDTOWithSameEmail() {
        User user = new User();
        user.setEmail("email@test.com");
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    public void shouldReturnUserDTOWithSameName() {
        User user = new User();
        user.setName("name");
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getName(), userDTO.getName());
    }

    @Test
    public void shouldReturnUserDTOWithSameSurname() {
        User user = new User();
        user.setSurname("surname");
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getSurname(), userDTO.getSurname());
    }

    @Test
    public void shouldReturnUserDTOWithSamePatronymic() {
        User user = new User();
        user.setPatronymic("patronymic");
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getPatronymic(), userDTO.getPatronymic());
    }

    @Test
    public void shouldReturnUserDTOWithSamePassword() {
        User user = new User();
        user.setPassword("password");
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test
    public void shouldReturnUserDTOWithSameUnchangeableStatus() {
        User user = new User();
        user.setUnchangeable(true);
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getUnchangeable(), userDTO.getUnchangeable());
    }

    @Test
    public void shouldReturnUserDTOWithSameDeletedStatus() {
        User user = new User();
        user.setDeleted(true);
        UserDTO userDTO = userConverter.toDTO(user);
        assertEquals(user.getDeleted(), userDTO.getDeleted());
    }

    @Test
    public void shouldReturnUserWithSameID() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getId(), user.getId());
    }

    @Test
    public void shouldReturnUserWithSameEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("email@test.com");
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    public void shouldReturnUserWithSameName() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("name");
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getName(), user.getName());
    }

    @Test
    public void shouldReturnUserWithSameSurname() {
        UserDTO userDTO = new UserDTO();
        userDTO.setSurname("surname");
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getSurname(), user.getSurname());
    }

    @Test
    public void shouldReturnUserWithSamePatronymic() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPatronymic("patronymic");
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getPatronymic(), user.getPatronymic());
    }

    @Test
    public void shouldReturnUserWithSamePassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("password");
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getPassword(), user.getPassword());
    }

    @Test
    public void shouldReturnUserWithSameUnchangeableStatus() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUnchangeable(true);
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getUnchangeable(), user.getUnchangeable());
    }

    @Test
    public void shouldReturnUserWithSameDeletedStatus() {
        UserDTO userDTO = new UserDTO();
        userDTO.setDeleted(true);
        User user = userConverter.fromDTO(userDTO);
        assertEquals(userDTO.getDeleted(), user.getDeleted());
    }
}
