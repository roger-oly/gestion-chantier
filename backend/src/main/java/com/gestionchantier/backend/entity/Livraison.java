package com.gestionchantier.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private Chantier chantier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

    @JsonProperty("idChantier")
    public Integer getIdChantier() {
        return chantier != null
                ? chantier.getIdChantier()
                : null;
    }

    @JsonProperty("idUtilisateur")
    public Integer getIdUtilisateur() {
        return utilisateur != null
                ? utilisateur.getIdUtilisateur()
                : null;
    }
}