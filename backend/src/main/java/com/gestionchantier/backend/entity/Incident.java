package com.gestionchantier.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident")
    private Integer idIncident;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "gravite", length = 20)
    private String gravite;

    @Column(name = "date_incident", nullable = false)
    private LocalDateTime dateIncident;

    @Column(name = "statut", nullable = false, length = 30)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chantier", nullable = false)
    private Chantier chantier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}
