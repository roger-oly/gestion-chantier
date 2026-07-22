package com.gestionchantier.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "livraison")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livraison")
    private Integer idLivraison;

    @Column(name = "description")
    private String description;

    @Column(name = "date_livraison", nullable = false)
    private LocalDate dateLivraison;

    @Column(name = "statut", nullable = false, length = 30)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chantier", nullable = false)
    private Chantier chantier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}
