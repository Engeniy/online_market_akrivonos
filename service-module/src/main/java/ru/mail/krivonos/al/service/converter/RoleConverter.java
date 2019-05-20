package ru.mail.krivonos.al.service.converter;

import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.service.model.RoleDTO;

public interface RoleConverter {

    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO roleDTO);
}
