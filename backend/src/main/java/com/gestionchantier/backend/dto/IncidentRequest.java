package com.gestionchantier.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IncidentRequest {

    private String type;
    private String description;
    private String gravite;
    private LocalDateTime dateIncident;
    private String statut;

    private Integer idChantier;
    private Integer idUtilisateur;
}