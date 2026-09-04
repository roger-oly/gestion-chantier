package com.gestionchantier.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LivraisonRequest {

    private String description;

    private LocalDate dateLivraison;

    private String statut;

    private Integer idChantier;

    private Integer idUtilisateur;
}