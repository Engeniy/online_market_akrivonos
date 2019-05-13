package ru.mail.krivonos.al.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.ADMIN_AUTHORITY_NAME;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerSecureIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldSucceedForUsersPage() throws Exception {
        mockMvc.perform(get("/private/users")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldFailForUsersPage() throws Exception {
        mockMvc.perform(get("/private/users")).andExpect(MockMvcResultMatchers.status().isFound());
    }

    @WithMockUser(authorities = {ADMIN_AUTHORITY_NAME})
    @Test
    public void shouldSucceedForAddUserPage() throws Exception {
        mockMvc.perform(get("/private/users/add")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldFailForAddUserPage() throws Exception {
        mockMvc.perform(get("/private/users/add")).andExpect(MockMvcResultMatchers.status().isFound());
    }
}
