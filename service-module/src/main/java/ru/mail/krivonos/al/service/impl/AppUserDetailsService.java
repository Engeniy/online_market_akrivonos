package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.model.AuthUserPrincipal;
import ru.mail.krivonos.al.service.model.UserDTO;

@Service("userDetailsService")
public class AppUserDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Username \"%s\" not found.";

    private final UserService userService;

    @Autowired
    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userByEmail = userService.getUserByEmail(username);
        if (userByEmail == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_ERROR_MESSAGE, username));
        }
        return new AuthUserPrincipal(userByEmail);
    }
}
