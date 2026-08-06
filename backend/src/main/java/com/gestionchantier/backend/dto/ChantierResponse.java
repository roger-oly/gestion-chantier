package com.gestionchantier.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ChantierResponse {

    private Integer idChantier;

    private String nom;

    private String description;

    private String localisation;

    private BigDecimal budget;

    private LocalDate dateDebut;

    private LocalDate dateFinPrevue;

    private String statut;

    private Integer idUtilisateur;

    private String nomUtilisateur;

}