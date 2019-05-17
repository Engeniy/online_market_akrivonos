package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleRepository extends GenericRepository<Long, Role> {

    List<Role> findRoles(Connection connection);
}
