package com.gestionchantier.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "avancement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avancement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avancement")
    private Integer idAvancement;

    @Column(name = "pourcentage", nullable = false)
    private Integer pourcentage;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "date_mise_a_jour", nullable = false)
    private LocalDateTime dateMiseAJour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tache", nullable = false)
    private Tache tache;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}
