package com.gestionchantier.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "chantier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chantier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chantier")
    private Integer idChantier;

    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "localisation", nullable = false, length = 255)
    private String localisation;

    @Column(name = "budget", precision = 12, scale = 2)
    private BigDecimal budget;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin_prevue")
    private LocalDate dateFinPrevue;

    @Column(name = "statut", nullable = false, length = 30)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}
