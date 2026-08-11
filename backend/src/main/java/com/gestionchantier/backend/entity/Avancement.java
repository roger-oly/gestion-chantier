package com.gestionchantier.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JoinColumn(name = "id_chantier", nullable = false)
@JsonIgnore
private Chantier chantier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

}
