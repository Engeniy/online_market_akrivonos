package ru.mail.krivonos.al.repository.impl;

import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.RoleRepository;
import ru.mail.krivonos.al.repository.model.Role;

@Repository("roleRepository")
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {
}
