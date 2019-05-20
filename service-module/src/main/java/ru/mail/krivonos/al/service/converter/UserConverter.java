package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.model.UserDTO;

public interface UserConverter {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
