package com.gestionchantier.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tache")
    private Integer idTache;

    @Column(name = "titre", nullable = false, length = 150)
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "statut", nullable = false, length = 30)
    private String statut;

    @Column(name = "niveau_priorite", length = 20)
    private String niveauPriorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chantier", nullable = false)
    private Chantier chantier;

}