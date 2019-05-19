package ru.mail.krivonos.al.service.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthUserPrincipal implements UserDetails {

    private UserDTO userDTO;
    private Set<GrantedAuthority> authorities;

    public AuthUserPrincipal(UserDTO userDTO) {
        this.userDTO = userDTO;
        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(userDTO.getRole().getName()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !userDTO.getDeleted();
    }

    public Long getUserID() {
        return userDTO.getId();
    }
}
