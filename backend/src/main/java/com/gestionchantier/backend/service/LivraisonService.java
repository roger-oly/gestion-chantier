package com.gestionchantier.backend.service;

import com.gestionchantier.backend.dto.LivraisonRequest;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Livraison;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.LivraisonRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final ChantierRepository chantierRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;

    public LivraisonService(
            LivraisonRepository livraisonRepository,
            ChantierRepository chantierRepository,
            UtilisateurRepository utilisateurRepository,
            NotificationService notificationService) {

        this.livraisonRepository = livraisonRepository;
        this.chantierRepository = chantierRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationService = notificationService;
    }

    /**
     * Récupère toutes les livraisons.
     */
    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    /**
     * Récupère une livraison par son identifiant.
     */
    public Livraison getLivraisonById(Integer id) {
        return livraisonRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Livraison introuvable"));
    }

    /**
     * Récupère les livraisons d'un chantier.
     */
    public List<Livraison> getLivraisonsByChantier(Integer idChantier) {
        return livraisonRepository.findByChantier_IdChantier(idChantier);
    }

    /**
     * Crée une livraison.
     *
     * Après création, une notification est envoyée
     * à l'utilisateur responsable du chantier.
     */
    public Livraison saveLivraison(LivraisonRequest request) {

        if (request.getIdChantier() == null) {
            throw new IllegalArgumentException(
                    "Le chantier est obligatoire.");
        }

        if (request.getIdUtilisateur() == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur est obligatoire.");
        }

        Chantier chantier = chantierRepository
                .findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"));

        Utilisateur utilisateur = utilisateurRepository
                .findById(request.getIdUtilisateur())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"));

        Livraison livraison = new Livraison();

        livraison.setDescription(request.getDescription());
        livraison.setDateLivraison(request.getDateLivraison());
        livraison.setStatut(request.getStatut());
        livraison.setChantier(chantier);
        livraison.setUtilisateur(utilisateur);

        /*
         * Enregistrement de la livraison.
         */
        Livraison savedLivraison =
                livraisonRepository.save(livraison);

        /*
         * Détermination du destinataire.
         *
         * Pour l'instant :
         * utilisateur associé au chantier.
         *
         * Cette logique pourra évoluer plus tard
         * vers plusieurs destinataires.
         */
        if (chantier.getUtilisateur() != null
                && chantier.getUtilisateur().getIdUtilisateur() != null) {

            Integer idDestinataire =
                    chantier.getUtilisateur().getIdUtilisateur();

            notificationService.createNotification(
                    idDestinataire,
                    "Nouvelle livraison",
                    "Une nouvelle livraison a été enregistrée sur le chantier "
                            + chantier.getNom(),
                    "LIVRAISON"
            );
        }

        return savedLivraison;
    }

    /**
     * Modifie une livraison.
     */
    public Livraison updateLivraison(
            Integer id,
            LivraisonRequest request) {

        Livraison existingLivraison = livraisonRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Livraison introuvable"));

        if (request.getIdChantier() == null) {
            throw new IllegalArgumentException(
                    "Le chantier est obligatoire.");
        }

        if (request.getIdUtilisateur() == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur est obligatoire.");
        }

        Chantier chantier = chantierRepository
                .findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"));

        Utilisateur utilisateur = utilisateurRepository
                .findById(request.getIdUtilisateur())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"));

        existingLivraison.setDescription(request.getDescription());
        existingLivraison.setDateLivraison(request.getDateLivraison());
        existingLivraison.setStatut(request.getStatut());
        existingLivraison.setChantier(chantier);
        existingLivraison.setUtilisateur(utilisateur);

        return livraisonRepository.save(existingLivraison);
    }

    /**
     * Supprime une livraison.
     */
    public void deleteLivraison(Integer id) {

        Livraison livraison = livraisonRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Livraison introuvable"));

        livraisonRepository.delete(livraison);
    }
}