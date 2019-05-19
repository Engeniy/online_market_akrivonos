package ru.mail.krivonos.al.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.krivonos.al.repository.RoleRepository;
import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.converter.RoleConverter;
import ru.mail.krivonos.al.service.model.RoleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    @Autowired
    public RoleServiceImpl(
            RoleRepository roleRepository,
            RoleConverter roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    @Transactional
    public List<RoleDTO> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return getRoleDTOs(roles);
    }

    private List<RoleDTO> getRoleDTOs(List<Role> roles) {
        return roles.stream()
                .map(roleConverter::toDTO)
                .collect(Collectors.toList());
    }
}
