package com.gestionchantier.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Integer idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String role;

}