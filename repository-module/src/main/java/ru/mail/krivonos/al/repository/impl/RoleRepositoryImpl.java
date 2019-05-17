package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.RoleRepository;
import ru.mail.krivonos.al.repository.exceptions.RoleRepositoryException;
import ru.mail.krivonos.al.repository.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.mail.krivonos.al.repository.constant.RepositoryMessageConstants.QUERY_EXECUTION_ERROR_MESSAGE;
import static ru.mail.krivonos.al.repository.constant.RepositoryMessageConstants.RESULT_SET_CLOSING_ERROR_MESSAGE;

@Repository("roleRepository")
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {

    private static final String ROLES_EXTRACTION_ERROR_MESSAGE = "Error while extracting roles.";

    private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Override
    public List<Role> findRoles(Connection connection) {
        String sql = "SELECT * FROM Role";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Role> roles = new ArrayList<>();
                while (resultSet.next()) {
                    Role role = getRole(resultSet);
                    roles.add(role);
                }
                return roles;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw new RoleRepositoryException(RESULT_SET_CLOSING_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RoleRepositoryException(String.format(QUERY_EXECUTION_ERROR_MESSAGE, sql), e);
        }
    }

    private Role getRole(ResultSet resultSet) {
        try {
            Role role = new Role();
            role.setId(resultSet.getLong("id"));
            role.setName(resultSet.getString("name"));
            return role;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RoleRepositoryException(ROLES_EXTRACTION_ERROR_MESSAGE, e);
        }
    }
}
