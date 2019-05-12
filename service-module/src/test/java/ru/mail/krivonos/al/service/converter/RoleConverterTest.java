package ru.mail.krivonos.al.service.converter;

import org.junit.Test;
import ru.mail.krivonos.al.repository.model.Role;
import ru.mail.krivonos.al.service.converter.impl.RoleConverterImpl;
import ru.mail.krivonos.al.service.model.RoleDTO;

import static org.junit.Assert.assertEquals;

public class RoleConverterTest {

    private RoleConverter roleConverter = new RoleConverterImpl();

    @Test
    public void shouldReturnRoleDTOWithSameID() {
        Role role = new Role();
        role.setId(1L);
        RoleDTO roleDTO = roleConverter.toDTO(role);
        assertEquals(role.getId(), roleDTO.getId());
    }

    @Test
    public void shouldReturnRoleDTOWithSameName() {
        Role role = new Role();
        role.setName("name");
        RoleDTO roleDTO = roleConverter.toDTO(role);
        assertEquals(role.getName(), roleDTO.getName());
    }

    @Test
    public void shouldReturnRoleWithSameID() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        Role role = roleConverter.fromDTO(roleDTO);
        assertEquals(roleDTO.getId(), role.getId());
    }

    @Test
    public void shouldReturnRoleWithSameName() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("name");
        Role role = roleConverter.fromDTO(roleDTO);
        assertEquals(roleDTO.getName(), role.getName());
    }
}
