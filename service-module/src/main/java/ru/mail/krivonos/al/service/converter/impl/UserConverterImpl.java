package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.converter.RoleConverter;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.model.UserDTO;

@Component("userConverter")
public class UserConverterImpl implements UserConverter {

    private final RoleConverter roleConverter;

    @Autowired
    public UserConverterImpl(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setPatronymic(user.getPatronymic());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(roleConverter.toDTO(user.getRole()));
        userDTO.setUnchangeable(user.getUnchangeable());
        userDTO.setDeleted(user.getDeleted());
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setPatronymic(userDTO.getPatronymic());
        user.setPassword(userDTO.getPassword());
        user.setRole(roleConverter.fromDTO(userDTO.getRole()));
        user.setDeleted(userDTO.getDeleted());
        user.setUnchangeable(userDTO.getUnchangeable());
        return user;
    }
}
