package ru.mail.krivonos.al.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.mail.krivonos.al.service.impl.AppUserDetailsService;
import ru.mail.krivonos.al.service.model.RoleDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppUserDetailsServiceTest {

    @Mock
    private UserService userService;

    private UserDetailsService userDetailsService;

    @Before
    public void init() {
        userDetailsService = new AppUserDetailsService(userService);
    }

    @Test
    public void shouldReturnUserDetailsWithSameUsername() {
        String email = "email";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("Admin");
        userDTO.setRole(roleDTO);
        when(userService.getUserByEmail(email)).thenReturn(userDTO);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    public void shouldReturnUserDetailsWithSamePassword() {
        String email = "email";
        String password = "password";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("Admin");
        userDTO.setRole(roleDTO);
        userDTO.setPassword(password);
        when(userService.getUserByEmail(email)).thenReturn(userDTO);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    public void shouldReturnUserDetailsWithSameAuthority() {
        String email = "email";
        String authority = "Admin";
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(authority);
        userDTO.setRole(roleDTO);
        when(userService.getUserByEmail(email)).thenReturn(userDTO);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        boolean matches = false;
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(authority)) {
                matches = true;
            }
        }
        assertTrue(matches);
    }
}
