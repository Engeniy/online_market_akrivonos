package ru.mail.krivonos.al.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.krivonos.al.repository.RoleRepository;
import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.converter.RoleConverter;
import ru.mail.krivonos.al.service.exceptions.ConnectionAutoCloseException;
import ru.mail.krivonos.al.service.exceptions.RoleServiceException;
import ru.mail.krivonos.al.service.model.RoleDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.krivonos.al.service.constant.ServiceMessageConstants.CONNECTION_CLOSE_ERROR_MESSAGE;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private static final String ROLES_GETTING_EXCEPTION_MESSAGE = "Error while getting roles list from data source.";

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleConverter roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    public List<RoleDTO> getRoles() {
        try (Connection connection = roleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Role> roles = roleRepository.findRoles(connection);
                List<RoleDTO> roleDTOs = getRoleDTOs(roles);
                connection.commit();
                return roleDTOs;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RoleServiceException(ROLES_GETTING_EXCEPTION_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionAutoCloseException(CONNECTION_CLOSE_ERROR_MESSAGE, e);
        }
    }

    private List<RoleDTO> getRoleDTOs(List<Role> roles) {
        return roles.stream()
                .map(roleConverter::toDTO)
                .collect(Collectors.toList());
    }
}
