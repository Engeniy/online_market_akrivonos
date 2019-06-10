package ru.mail.krivonos.al.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.krivonos.al.service.RoleService;
import ru.mail.krivonos.al.service.model.RoleDTO;

import java.util.List;

import static ru.mail.krivonos.al.controller.constant.ApiURLConstants.API_ROLES_URL;

@RestController("roleApiController")
public class RoleApiController {

    private final RoleService roleService;

    @Autowired
    public RoleApiController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(API_ROLES_URL)
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        List<RoleDTO> roles = roleService.getRoles();
        return new ResponseEntity(roles, HttpStatus.OK);
    }
}
