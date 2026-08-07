package com.gestionchantier.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChantierRequest {

    @NotBlank(message = "Le nom du chantier est obligatoire")
    private String nom;

    private String description;

    @NotBlank(message = "La localisation est obligatoire")
    private String localisation;

    @NotNull(message = "Le budget est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le budget doit être supérieur ou égal à 0")
    private BigDecimal budget;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin prévue est obligatoire")
    private LocalDate dateFinPrevue;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    @NotNull(message = "L'utilisateur est obligatoire")
    private Integer idUtilisateur;
}