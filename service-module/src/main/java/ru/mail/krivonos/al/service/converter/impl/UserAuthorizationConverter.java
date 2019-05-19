package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.converter.ProfileConverter;
import ru.mail.krivonos.al.service.converter.RoleConverter;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.model.UserDTO;

@Component("userAuthorizationConverter")
public class UserAuthorizationConverter implements UserConverter {

    private final RoleConverter roleConverter;
    private final ProfileConverter profileConverter;

    @Autowired
    public UserAuthorizationConverter(
            RoleConverter roleConverter,
            ProfileConverter profileConverter
    ) {
        this.roleConverter = roleConverter;
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(roleConverter.toDTO(user.getRole()));
        userDTO.setDeleted(user.isDeleted());
        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setPassword(userDTO.getPassword());
        user.setRole(roleConverter.toEntity(userDTO.getRole()));
        user.setProfile(profileConverter.toEntity(userDTO.getProfile()));
        return user;
    }
}
