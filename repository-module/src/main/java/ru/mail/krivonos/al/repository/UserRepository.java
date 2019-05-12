package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {

    List<User> findUsers(Connection connection, Integer pageNumber);

    User findUserByEmail(Connection connection, String email);

    User findUserByID(Connection connection, Long id);

    int updateRole(Connection connection, Long id, String roleName);

    void saveUser(Connection connection, User user);

    int countUsers(Connection connection);

    int deleteUsers(Connection connection, Long[] userIDs);

    int changePassword(Connection connection, Long userID, String password);
}
