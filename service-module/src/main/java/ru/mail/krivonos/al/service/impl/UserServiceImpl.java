package ru.mail.krivonos.al.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.model.User;
import ru.mail.krivonos.al.service.PageCountingService;
import ru.mail.krivonos.al.service.PasswordService;
import ru.mail.krivonos.al.service.UserService;
import ru.mail.krivonos.al.service.converter.UserConverter;
import ru.mail.krivonos.al.service.exceptions.ConnectionAutoCloseException;
import ru.mail.krivonos.al.service.exceptions.UserServiceException;
import ru.mail.krivonos.al.service.model.UserDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.repository.constant.LimitConstants.USERS_LIMIT;
import static ru.mail.krivonos.al.service.constant.ServiceMessageConstants.CONNECTION_CLOSE_ERROR_MESSAGE;
import static ru.mail.krivonos.al.service.constant.ServiceMessageConstants.PAGES_COUNTING_EXCEPTION_MESSAGE;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String USERS_GETTING_EXCEPTION_MESSAGE = "Error while getting users list from data source.";
    private static final String USER_GETTING_EXCEPTION_MESSAGE = "Error while getting user from data source.";
    private static final String USERS_DELETING_EXCEPTION_MESSAGE = "Error while deleting users.";
    private static final String USER_ROLE_UPDATING_EXCEPTION_MESSAGE = "Error while updating user role.";
    private static final String USER_SAVING_EXCEPTION_MESSAGE = "Error while saving user.";
    private static final String PASSWORD_CHANGING_EXCEPTION_MESSAGE = "Error while changing password.";

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordService passwordService;
    private final PageCountingService pageCountingService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserConverter userConverter,
            PasswordService passwordService,
            PageCountingService pageCountingService
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordService = passwordService;
        this.pageCountingService = pageCountingService;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User userByEmail = userRepository.findUserByEmail(connection, email);
                if (userByEmail != null) {
                    UserDTO userDTO = userConverter.toDTO(userByEmail);
                    connection.commit();
                    return userDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(USER_GETTING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public List<UserDTO> getUsers(Integer pageNumber) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<User> users = userRepository.findUsers(connection, pageNumber);
                List<UserDTO> userDTOs = getUserDTOs(users);
                connection.commit();
                return userDTOs;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(USERS_GETTING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int updateRole(Long id, String roleName) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int updated = userRepository.updateRole(connection, id, roleName);
                connection.commit();
                return updated;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(USER_ROLE_UPDATING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void add(UserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userConverter.fromDTO(userDTO);
                user.setPassword(passwordService.getPassword());
                userRepository.saveUser(connection, user);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(USER_SAVING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int getPagesNumber() {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int usersNumber = userRepository.countUsers(connection);
                int pagesNumber = pageCountingService.countPages(usersNumber, USERS_LIMIT);
                connection.commit();
                return pagesNumber;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(PAGES_COUNTING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int deleteUsers(Long[] userIDs) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int deleted = userRepository.deleteUsers(connection, userIDs);
                connection.commit();
                return deleted;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(USERS_DELETING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int changePassword(Long userID) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User userByID = userRepository.findUserByID(connection, userID);
                if (userByID == null) {
                    connection.commit();
                    return 0;
                }
                String password = passwordService.getPassword();
                int changed = userRepository.changePassword(connection, userID, password);
                connection.commit();
                return changed;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(PASSWORD_CHANGING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    private List<UserDTO> getUserDTOs(List<User> users) {
        return users.stream()
                .map(userConverter::toDTO)
                .peek(this::preparePatronymic)
                .collect(Collectors.toList());
    }

    private void preparePatronymic(UserDTO userDTO) {
        if (userDTO.getPatronymic() == null) {
            userDTO.setPatronymic("");
        }
    }
}
