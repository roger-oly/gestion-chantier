package com.gestionchantier.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Integer idDocument;

    private String nom;

    private String type;

    private String cheminFichier;

    private LocalDateTime dateUpload;

    private Integer idChantier;

    private String nomChantier;

    private Integer idUtilisateur;

    private String nomUtilisateur;
}