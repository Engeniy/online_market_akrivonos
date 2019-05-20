package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.PageDTO;
import ru.mail.krivonos.al.service.model.UserDTO;

public interface UserService {

    UserDTO getUserByEmail(String email);

    PageDTO<UserDTO> getUsers(Integer pageNumber);

    void updateRole(Long userID, Long roleID);

    void add(UserDTO userDTO);

    void deleteUsers(Long[] userIDs);

    void changePassword(Long userID);

    UserDTO getUserByID(Long userID);

    void updateProfile(UserDTO userDTO);

    void updatePassword(UserDTO userDTO);
}
