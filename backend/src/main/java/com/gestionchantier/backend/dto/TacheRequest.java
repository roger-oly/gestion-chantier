package com.gestionchantier.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TacheRequest {

    private String titre;

    private String description;

    private String statut;

    private String niveauPriorite;

    private Integer idChantier;
}