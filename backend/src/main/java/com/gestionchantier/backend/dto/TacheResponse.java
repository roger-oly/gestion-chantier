package com.gestionchantier.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TacheResponse {

    private Integer idTache;

    private String titre;

    private String description;

    private String statut;

    private String niveauPriorite;

    private Integer idChantier;

    private String nomChantier;
}