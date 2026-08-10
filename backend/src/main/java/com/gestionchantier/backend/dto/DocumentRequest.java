package com.gestionchantier.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    private String nom;

    private String type;

    private String cheminFichier;

    private Integer idChantier;

    private Integer idUtilisateur;
}