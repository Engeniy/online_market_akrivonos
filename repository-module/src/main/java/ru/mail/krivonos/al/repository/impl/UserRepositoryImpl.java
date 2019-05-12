package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.UserRepository;
import ru.mail.krivonos.al.repository.exceptions.UserRepositoryException;
import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.repository.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.krivonos.al.repository.constant.LimitConstants.USERS_LIMIT;
import static ru.mail.krivonos.al.repository.constant.RepositoryMessageConstants.QUERY_EXECUTION_ERROR_MESSAGE;
import static ru.mail.krivonos.al.repository.constant.RepositoryMessageConstants.RESULT_SET_CLOSING_ERROR_MESSAGE;

@Repository("userRepository")
public class UserRepositoryImpl extends GenericRepositoryImpl implements UserRepository {

    private static final String USER_EXTRACTION_ERROR_MESSAGE = "Error while extracting user.";
    private static final String USERS_COUNTING_ERROR_MESSAGE = "Error while counting users.";
    private static final String USERS_DELETING_ERROR_MESSAGE = "Error while deleting users.";
    private static final String PASSWORD_UPDATING_ERROR_MESSAGE = "Error while updating password.";

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public List<User> findUsers(Connection connection, Integer pageNumber) {
        String sql = "SELECT u.id, u.email, u.password, u.name, u.surname, u.patronymic, u.unchangeable, r.name as role_name " +
                "FROM User u JOIN Role r ON u.role_id = r.id WHERE deleted = FALSE ORDER BY u.email LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, USERS_LIMIT);
            preparedStatement.setInt(2, (pageNumber - 1) * USERS_LIMIT);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    User user = getUser(resultSet);
                    users.add(user);
                }
                return users;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new UserRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public User findUserByEmail(Connection connection, String email) {
        String sql = "SELECT u.id, u.email, u.password, u.deleted, r.name FROM User u JOIN Role r ON u.role_id = r.id " +
                "WHERE u.email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getUserForAuthorization(resultSet);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new UserRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public User findUserByID(Connection connection, Long id) {
        String sql = "SELECT u.id, u.email, u.password, u.deleted, r.name FROM User u JOIN Role r ON u.role_id = r.id " +
                "WHERE u.id = ? AND deleted = FALSE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getUserForAuthorization(resultSet);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new UserRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public int updateRole(Connection connection, Long id, String roleName) {
        String sql = "UPDATE User u SET u.role_id = (SELECT r.id FROM Role r WHERE r.name = ?) WHERE u.id = ? " +
                "AND unchangeable = FALSE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roleName);
            preparedStatement.setLong(2, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public void saveUser(Connection connection, User user) {
        String sql = "INSERT INTO User (email, name, surname, patronymic, password, role_id) VALUES (?, ?, ?, ?, ?, " +
                "(SELECT r.id FROM Role r WHERE r.name = ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getPatronymic());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getRole().getName());
            int added = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public int countUsers(Connection connection) {
        String sql = "SELECT COUNT(*) FROM User WHERE deleted = FALSE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getUsersNumber(resultSet);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new UserRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    @Override
    public int deleteUsers(Connection connection, Long[] userIDs) {
        String sql = "UPDATE User SET deleted = TRUE WHERE id = ? AND unchangeable = FALSE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            addQueries(userIDs, preparedStatement);
            int[] batchExecutionResult = preparedStatement.executeBatch();
            return countUpdatedRows(batchExecutionResult);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(USERS_DELETING_ERROR_MESSAGE, e);
        }
    }

    @Override
    public int changePassword(Connection connection, Long userID, String password) {
        String sql = "UPDATE User SET password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, password);
            preparedStatement.setLong(2, userID);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(PASSWORD_UPDATING_ERROR_MESSAGE, e);
        }
    }

    private User getUser(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            user.setPatronymic(resultSet.getString("patronymic"));
            user.setUnchangeable(resultSet.getBoolean("unchangeable"));
            Role role = new Role();
            role.setName(resultSet.getString("role_name"));
            user.setRole(role);
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(USER_EXTRACTION_ERROR_MESSAGE, e);
        }
    }

    private User getUserForAuthorization(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setDeleted(resultSet.getBoolean("deleted"));
                Role role = new Role();
                role.setName(resultSet.getString("name"));
                user.setRole(role);
                return user;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(USER_EXTRACTION_ERROR_MESSAGE, e);
        }
        return null;
    }

    private int getUsersNumber(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(USERS_COUNTING_ERROR_MESSAGE, e);
        }
        return 0;
    }

    private void addQueries(Long[] userIDs, PreparedStatement preparedStatement) throws SQLException {
        for (Long userID : userIDs) {
            preparedStatement.setLong(1, userID);
            preparedStatement.addBatch();
        }
    }

    private int countUpdatedRows(int[] batchExecutionResult) {
        int updatedRows = 0;
        for (int result : batchExecutionResult) {
            updatedRows += result;
        }
        return updatedRows;
    }
}
