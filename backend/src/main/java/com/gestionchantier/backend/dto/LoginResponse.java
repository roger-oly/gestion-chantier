package com.gestionchantier.backend.dto;

import com.gestionchantier.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Integer idUtilisateur;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String statut;

    private Role role;
}