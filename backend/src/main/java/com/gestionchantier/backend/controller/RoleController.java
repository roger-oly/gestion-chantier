package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Role;
import com.gestionchantier.backend.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping
    public Role saveRole(@RequestBody Role role) {
    return roleService.saveRole(role);
    }

    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Integer id, @RequestBody Role role) {
    return roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Integer id) {
    roleService.deleteRole(id);
    }
}