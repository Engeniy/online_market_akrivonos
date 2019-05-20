package ru.mail.krivonos.al.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.krivonos.al.service.model.ProfileDTO;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserAPIControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("save@save.com");
        userDTO.setName("Alex");
        userDTO.setSurname("Krivonos");
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setTelephone("+375291111111");
        profileDTO.setAddress("Address");
        userDTO.setProfile(profileDTO);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        userDTO.setRole(roleDTO);
        restTemplate.withBasicAuth("api@api.com", "admin");
        ResponseEntity responseEntity = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .postForEntity("http://localhost:8080/api/v1/users", userDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void shouldGetBadRequestForExistingUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("admin@admin.com");
        userDTO.setName("Alex");
        userDTO.setSurname("Krivonos");
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setTelephone("+375291111111");
        profileDTO.setAddress("Address");
        userDTO.setProfile(profileDTO);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(2L);
        userDTO.setRole(roleDTO);
        restTemplate.withBasicAuth("api@api.com", "admin");
        ResponseEntity responseEntity = restTemplate
                .withBasicAuth("api@api.com", "admin")
                .postForEntity("http://localhost:8080/api/v1/users", userDTO, ResponseEntity.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
