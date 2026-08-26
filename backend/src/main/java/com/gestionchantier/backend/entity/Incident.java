package com.gestionchantier.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Column(name = "date_modification", nullable = false)
private LocalDateTime dateModification;


    @Column(name = "statut", nullable = false, length = 30)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chantier", nullable = false)
    @JsonIgnore
    private Chantier chantier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

    @JsonProperty("idChantier")
public Integer getIdChantier() {
    return chantier != null ? chantier.getIdChantier() : null;
}

}
