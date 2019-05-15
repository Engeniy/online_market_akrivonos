package ru.mail.krivonos.al.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.mail.krivonos.al.repository.RoleRepository;
import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.service.converter.RoleConverter;
import ru.mail.krivonos.al.service.exceptions.RoleServiceException;
import ru.mail.krivonos.al.service.impl.RoleServiceImpl;
import ru.mail.krivonos.al.service.model.RoleDTO;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private Connection connection;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleConverter roleConverter;

    private RoleService roleService;

    @Before
    public void init() {
        roleService = new RoleServiceImpl(roleRepository, roleConverter);
        when(roleRepository.getConnection()).thenReturn(connection);
    }

    @Test
    public void shouldReturnRoleDTOListFromGetRolesMethod() {
        Role role = new Role();
        RoleDTO roleDTO = new RoleDTO();
        List<Role> roles = Collections.singletonList(role);
        when(roleRepository.findRoles(connection)).thenReturn(roles);
        when(roleConverter.toDTO(role)).thenReturn(roleDTO);
        List<RoleDTO> result = roleService.getRoles();
        assertEquals(roleDTO, result.get(0));
    }

    @Test(expected = RoleServiceException.class)
    public void shouldThrowRoleServiceExceptionWhenCatchingExceptionFromRepositoryFromGetRolesMethod() {
        when(roleRepository.findRoles(connection)).thenThrow(new RuntimeException());
        roleService.getRoles();
    }
}
