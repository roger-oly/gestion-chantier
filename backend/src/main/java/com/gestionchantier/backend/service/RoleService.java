package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Role;
import com.gestionchantier.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Retourne tous les rôles.
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Enregistre un nouveau rôle.
     */
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Recherche un rôle par son identifiant.
     */
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rôle introuvable"));
    }

    /**
     * Met à jour un rôle.
     */
    public Role updateRole(Integer id, Role role) {
        Role existingRole = getRoleById(id);
        existingRole.setLibelle(role.getLibelle());
        return roleRepository.save(existingRole);
    }

    /**
     * Supprime un rôle.
     */
    public void deleteRole(Integer id) {
    Role existingRole = getRoleById(id);
    roleRepository.delete(existingRole);
    }
}