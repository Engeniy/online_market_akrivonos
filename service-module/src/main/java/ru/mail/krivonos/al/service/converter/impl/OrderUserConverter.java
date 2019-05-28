package ru.mail.krivonos.al.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.converter.ProfileConverterAggregator;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.model.UserDTO;

@Component("orderUserConverter")
public class OrderUserConverter implements UserConverter {

    private final ProfileConverterAggregator profileConverterAggregator;

    @Autowired
    public OrderUserConverter(ProfileConverterAggregator profileConverterAggregator) {
        this.profileConverterAggregator = profileConverterAggregator;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setProfile(profileConverterAggregator.getOrderUserProfileConverter().toDTO(user.getProfile()));
        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        return user;
    }
}
