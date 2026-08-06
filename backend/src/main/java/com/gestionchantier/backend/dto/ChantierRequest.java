package com.gestionchantier.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChantierRequest {

    private String nom;

    private String description;

    private String localisation;

    private BigDecimal budget;

    private LocalDate dateDebut;

    private LocalDate dateFinPrevue;

    private String statut;

    private Integer idUtilisateur;
}