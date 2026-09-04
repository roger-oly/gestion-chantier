package com.gestionchantier.backend.service;

import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.dto.ChantierResponse;
import com.gestionchantier.backend.dto.ChantierRequest;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChantierService {

    private final NotificationService notificationService;
    private final ChantierRepository chantierRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ChantierService(
            ChantierRepository chantierRepository,
            UtilisateurRepository utilisateurRepository,
            NotificationService notificationService
    ) {

        this.chantierRepository = chantierRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationService = notificationService;
    }

    /**
     * Retourne tous les chantiers.
     */
    public List<ChantierResponse> getAllChantiers() {

        return chantierRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retourne un chantier selon l'id.
     */
    public ChantierResponse getChantierById(Integer id) {

        Chantier chantier = chantierRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"
                        )
                );

        return ChantierResponse.builder()

                .idChantier(
                        chantier.getIdChantier()
                )

                .nom(
                        chantier.getNom()
                )

                .description(
                        chantier.getDescription()
                )

                .localisation(
                        chantier.getLocalisation()
                )

                .budget(
                        chantier.getBudget()
                )

                .dateDebut(
                        chantier.getDateDebut()
                )

                .dateFinPrevue(
                        chantier.getDateFinPrevue()
                )

                .statut(
                        chantier.getStatut()
                )

                .idUtilisateur(
                        chantier.getUtilisateur() != null
                                ? chantier.getUtilisateur()
                                        .getIdUtilisateur()
                                : null
                )

                .nomUtilisateur(
                        chantier.getUtilisateur() != null
                                ? chantier.getUtilisateur().getNom()
                                        + " "
                                        + chantier.getUtilisateur().getPrenom()
                                : null
                )

                .build();
    }

    /**
     * Enregistre un nouveau chantier.
     */
    public ChantierResponse saveChantier(
            ChantierRequest request
    ) {

        Utilisateur utilisateur =
                utilisateurRepository.findById(
                        request.getIdUtilisateur()
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        Chantier chantier = Chantier.builder()

                .nom(request.getNom())

                .description(request.getDescription())

                .localisation(request.getLocalisation())

                .budget(request.getBudget())

                .dateDebut(request.getDateDebut())

                .dateFinPrevue(request.getDateFinPrevue())

                .statut(request.getStatut())

                .utilisateur(utilisateur)

                .build();

        Chantier saved =
                chantierRepository.save(chantier);

        /*
         * Notification du responsable du chantier.
         *
         * Pour l'instant, le destinataire est
         * l'utilisateur associé au chantier.
         *
         * Cette logique pourra évoluer plus tard
         * vers plusieurs destinataires.
         */
        if (saved.getUtilisateur() != null
                && saved.getUtilisateur().getIdUtilisateur() != null) {

            Integer idDestinataire =
                    saved.getUtilisateur().getIdUtilisateur();

            notificationService.createNotification(
                    idDestinataire,
                    "Nouveau chantier",
                    "Le chantier "
                            + saved.getNom()
                            + " vous a été attribué.",
                    "CHANTIER"
            );
        }

        return ChantierResponse.builder()

                .idChantier(saved.getIdChantier())

                .nom(saved.getNom())

                .description(saved.getDescription())

                .localisation(saved.getLocalisation())

                .budget(saved.getBudget())

                .dateDebut(saved.getDateDebut())

                .dateFinPrevue(saved.getDateFinPrevue())

                .statut(saved.getStatut())

                .idUtilisateur(
                        utilisateur.getIdUtilisateur()
                )

                .nomUtilisateur(
                        utilisateur.getNom()
                                + " "
                                + utilisateur.getPrenom()
                )

                .build();
    }

    /**
     * Met à jour un chantier.
     */
    public ChantierResponse updateChantier(
            Integer id,
            ChantierRequest request) {

        Chantier existingChantier =
                chantierRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Chantier introuvable"
                                )
                        );

        /*
         * On mémorise l'ancien responsable
         * avant toute modification.
         */
        Integer ancienResponsableId =
                existingChantier.getUtilisateur() != null
                        ? existingChantier.getUtilisateur()
                                .getIdUtilisateur()
                        : null;

        /*
         * Récupération du nouveau responsable.
         */
        Utilisateur utilisateur =
                utilisateurRepository.findById(
                        request.getIdUtilisateur()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        existingChantier.setNom(request.getNom());
        existingChantier.setDescription(request.getDescription());
        existingChantier.setLocalisation(request.getLocalisation());
        existingChantier.setBudget(request.getBudget());
        existingChantier.setDateDebut(request.getDateDebut());
        existingChantier.setDateFinPrevue(request.getDateFinPrevue());
        existingChantier.setStatut(request.getStatut());
        existingChantier.setUtilisateur(utilisateur);

        Chantier updatedChantier =
                chantierRepository.save(existingChantier);

        /*
         * Si le responsable du chantier a changé,
         * notification du nouveau responsable.
         */
        Integer nouveauResponsableId =
                utilisateur.getIdUtilisateur();

        if (ancienResponsableId == null
                || !ancienResponsableId.equals(
                        nouveauResponsableId
                )) {

            notificationService.createNotification(
                    nouveauResponsableId,
                    "Chantier attribué",
                    "Le chantier "
                            + updatedChantier.getNom()
                            + " vous a été attribué.",
                    "CHANTIER"
            );
        }

        return toResponse(updatedChantier);
    }

    /**
     * Supprime un chantier.
     */
    public void deleteChantier(Integer id) {

        chantierRepository.deleteById(id);
    }

    /**
     * Transforme une entité Chantier en ChantierResponse.
     */
    private ChantierResponse toResponse(
            Chantier chantier) {

        return ChantierResponse.builder()

                .idChantier(
                        chantier.getIdChantier()
                )

                .nom(
                        chantier.getNom()
                )

                .description(
                        chantier.getDescription()
                )

                .localisation(
                        chantier.getLocalisation()
                )

                .budget(
                        chantier.getBudget()
                )

                .dateDebut(
                        chantier.getDateDebut()
                )

                .dateFinPrevue(
                        chantier.getDateFinPrevue()
                )

                .statut(
                        chantier.getStatut()
                )

                .idUtilisateur(
                        chantier.getUtilisateur() != null
                                ? chantier.getUtilisateur()
                                        .getIdUtilisateur()
                                : null
                )

                .nomUtilisateur(
                        chantier.getUtilisateur() != null
                                ? chantier.getUtilisateur().getNom()
                                        + " "
                                        + chantier.getUtilisateur().getPrenom()
                                : null
                )

                .build();
    }
}