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
import ru.mail.krivonos.al.controller.model.UserPasswordForm;
import ru.mail.krivonos.al.service.model.ProfileDTO;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.ADMIN_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.CUSTOMER_AUTHORITY_NAME;

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
        correctUserDTO.setId(2L);
        correctUserDTO.setEmail("krivonos-al@mail.ru");
        correctUserDTO.setName("Alex");
        correctUserDTO.setSurname("Krivonos");
        ProfileDTO correctProfile = new ProfileDTO();
        correctProfile.setAddress("Some address");
        correctProfile.setTelephone("+375441111111");
        correctUserDTO.setProfile(correctProfile);
        RoleDTO correctRole = new RoleDTO();
        correctRole.setId(1L);
        correctRole.setName("Sale User");
        correctUserDTO.setRole(correctRole);
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldReturnUsersPageForGetRequest() throws Exception {
        mockMvc.perform(get("/private/users")).andExpect(status().isOk());
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void requestForUsersPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/private/users")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("admin@admin.com")));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldReturnAddUserPageForGetRequest() throws Exception {
        mockMvc.perform(get("/private/users/add")).andExpect(status().isOk());
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void requestForAddUserPageSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/private/users/add")
                .accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Add user")));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToUsersFirstPageForSuccessfulAddPostRequest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?added"));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldReturnAddUserPageWithEmailFieldError() throws Exception {
        correctUserDTO.setEmail(null);
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Email must not be empty.")));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldReturnAddUserPageWithNameFieldError() throws Exception {
        correctUserDTO.setName(null);
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Name must not be empty.")));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldReturnAddUserPageWithSurnameFieldError() throws Exception {
        correctUserDTO.setSurname(null);
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(CoreMatchers.containsString("Surname must not be empty.")));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithPositiveParamForSuccessfulUpdateRolePostRequest()
            throws Exception {
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?added"));
        this.mockMvc.perform(post("/private/users/2/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?page=1&updated"));
    }


    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithPositiveParamForSuccessfulDeletePostRequest() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?added"));
        Long[] ids = new Long[1];
        Long id = 2L;
        this.mockMvc.perform(post("/private/users/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user_ids", id.toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?page=1&deleted"));
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToUsersFirstPageWithPositiveParamForSuccessfulPasswordChangePostRequest()
            throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(new RoleDTO());
        this.mockMvc.perform(post("/private/users/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO)
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?added"));
        this.mockMvc.perform(post("/private/users/2/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/users?page=1&password_changed"));
    }

    @WithMockUser(authorities = {CUSTOMER_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToProfilePageWithPositiveParamForSuccessfulUpdateProfilePostRequest()
            throws Exception {
        this.mockMvc.perform(post("/private/profile/1/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("user", correctUserDTO))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/profile?updated"));
    }

    @WithMockUser(authorities = {CUSTOMER_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToProfilePageWithPositiveParamForSuccessfulUpdatePasswordPostRequest()
            throws Exception {
        UserPasswordForm userPasswordForm = new UserPasswordForm();
        userPasswordForm.setOldPassword("admin");
        userPasswordForm.setNewPassword("admin1");
        this.mockMvc.perform(post("/private/profile/1/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("password_form", userPasswordForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/profile?password_changed"));
    }

    @WithMockUser(authorities = {CUSTOMER_AUTHORITY_NAME})
    @Test
    public void shouldSendRedirectToArticlePageWithPositiveParamForSuccessfulUpdatePasswordPostRequest()
            throws Exception {
        UserPasswordForm userPasswordForm = new UserPasswordForm();
        userPasswordForm.setOldPassword("admin");
        userPasswordForm.setNewPassword("admin1");
        this.mockMvc.perform(post("/private/profile/1/password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("password_form", userPasswordForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/private/profile?password_changed"));
    }
}
