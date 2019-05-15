package ru.mail.krivonos.al.service;

import ru.mail.krivonos.al.service.model.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getUserByEmail(String email);

    List<UserDTO> getUsers(Integer pageNumber);

    int updateRole(Long userID, Long roleID);

    void add(UserDTO userDTO);

    int getPagesNumber();

    int deleteUsers(Long[] userIDs);

    int changePassword(Long userID);
}
