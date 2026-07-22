package com.gestionchantier.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document")
    private Integer idDocument;

    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "chemin_fichier", nullable = false, length = 255)
    private String cheminFichier;

    @Column(name = "date_upload", nullable = false)
    private LocalDateTime dateUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chantier", nullable = false)
    private Chantier chantier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

}
