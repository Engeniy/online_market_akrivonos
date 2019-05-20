package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.converter.ProfileConverter;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.model.UserDTO;

@Component("userForProfileConverter")
public class UserForProfileConverter implements UserConverter {

    private final ProfileConverter profileConverter;

    @Autowired
    public UserForProfileConverter(ProfileConverter profileConverter) {
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setPassword(user.getPassword());
        userDTO.setProfile(profileConverter.toDTO(user.getProfile()));
        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        throw new UnsupportedOperationException("toEntity() method in unsupported for UserForProfileConverter class.");
    }
}
