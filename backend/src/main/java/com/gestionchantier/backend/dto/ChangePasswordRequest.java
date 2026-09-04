package com.gestionchantier.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    private String ancienMotDePasse;

    private String nouveauMotDePasse;
}