package ru.mail.krivonos.al.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private UserDTO correctUserDTO;

    @Before
    public void init() {
        correctUserDTO = new UserDTO();
        correctUserDTO.setEmail("krivonos-al@mail.ru");
        correctUserDTO.setName("Alex");
        correctUserDTO.setSurname("Krivonos");
        RoleDTO correctRole = new RoleDTO();
        correctRole.setName("Sale User");
        correctUserDTO.setRole(correctRole);
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldReturnUsersPageForGetRequestWithPageNumber() throws Exception {
        mockMvc.perform(get("/private/users/1")).andExpect(status().isOk());
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void requestForUsersPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/private/users/1")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("admin@admin.com")));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldRedirectToUsersPageForGetRequestWithoutPageNumber() throws Exception {
        mockMvc.perform(get("/private/users")).andExpect(redirectedUrl("/private/users/1"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldReturnAddUserPageForGetRequest() throws Exception {
        mockMvc.perform(get("/private/users/add")).andExpect(status().isOk());
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void requestForAddUserPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/private/users/add")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Add user")));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageForSuccessfulAddPostRequest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", correctUserDTO.getEmail())
                .param("name", correctUserDTO.getName())
                .param("surname", correctUserDTO.getSurname())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?added"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldReturnAddUserPageWithEmailFieldError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", correctUserDTO.getName())
                .param("surname", correctUserDTO.getSurname())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Email must not be empty.")));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldReturnAddUserPageWithNameFieldError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", correctUserDTO.getEmail())
                .param("surname", correctUserDTO.getSurname())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Name must not be empty.")));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldReturnAddUserPageWithSurnameFieldError() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", correctUserDTO.getName())
                .param("email", correctUserDTO.getEmail())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Surname must not be empty.")));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithPositiveParamForSuccessfulUpdateRolePostRequest()
            throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", correctUserDTO.getEmail())
                .param("name", correctUserDTO.getName())
                .param("surname", correctUserDTO.getSurname())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?added"));
        this.mockMvc.perform(post("/private/users/2/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?updated"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithNegativeParamForUnsuccessfulUpdateRolePostRequest()
            throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/100/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?updated_zero"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithPositiveParamForSuccessfulDeletePostRequest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", correctUserDTO.getEmail())
                .param("name", correctUserDTO.getName())
                .param("surname", correctUserDTO.getSurname())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?added"));
        Long[] ids = new Long[1];
        Long id = 2L;
        this.mockMvc.perform(post("/private/users/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user_ids", id.toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?deleted"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithNegativeParamForUnsuccessfulDeletePostRequest() throws Exception {
        Long id = 100L;
        this.mockMvc.perform(post("/private/users/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user_ids", id.toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?deleted_zero"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithPositiveParamForSuccessfulPasswordChangePostRequest()
            throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", correctUserDTO.getEmail())
                .param("name", correctUserDTO.getName())
                .param("surname", correctUserDTO.getSurname())
                .param("role.name", correctUserDTO.getRole().getName())
                .requestAttr("user", userDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?added"));
        this.mockMvc.perform(post("/private/users/2/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?password_changed"));
    }

    @WithMockUser(authorities = {"Administrator"})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithNegativeParamForUnsuccessfulPasswordChangePostRequest()
            throws Exception {
        this.mockMvc.perform(post("/private/users/100/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users/1?password_error"));
    }
}
