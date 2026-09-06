package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    /*
     * Administrateur et Direction :
     * accès à tous les chantiers.
     */
    if (estAdministrateurOuDirection()) {

        return chantierRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /*
     * Autres utilisateurs :
     * uniquement les chantiers qui leur sont attribués.
     */
    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    String emailUtilisateur = authentication.getName();

    return chantierRepository.findAll()
            .stream()
            .filter(chantier ->
                    chantier.getUtilisateur() != null
                            && emailUtilisateur.equals(
                                    chantier.getUtilisateur().getEmail()
                            )
            )
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

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    /*
     * Administrateur et Direction :
     * accès à tous les chantiers.
     */
    if (estAdministrateurOuDirection()) {
        return toResponse(chantier);
    }

    /*
     * Les autres utilisateurs ne peuvent consulter
     * que les chantiers qui leur sont attribués.
     */
    if (chantier.getUtilisateur() == null
            || authentication == null
            || !chantier.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Accès interdit à ce chantier"
        );
    }

    return toResponse(chantier);
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

    if (utilisateur.getRole() == null
            || !"RESPONSABLE_PROJET".equals(
                    utilisateur.getRole().getLibelle()
            )) {

        throw new AccessDeniedException(
                "Le chantier doit être attribué à un responsable de projet"
        );
    }

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

                        Authentication authentication =
        SecurityContextHolder
                .getContext()
                .getAuthentication();

if (!estAdministrateurOuDirection()) {

    if (authentication == null
            || existingChantier.getUtilisateur() == null
            || !existingChantier.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à modifier ce chantier"
        );
    }
}

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

                if (utilisateur.getRole() == null
        || !"RESPONSABLE_PROJET".equals(
                utilisateur.getRole().getLibelle()
        )) {

    throw new AccessDeniedException(
            "Le chantier doit être attribué à un responsable de projet"
    );
}

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

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null
            || authentication.getAuthorities()
                    .stream()
                    .noneMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR"))) {

        throw new AccessDeniedException(
                "Seul un administrateur peut supprimer un chantier"
        );
    }

    Chantier chantier =
            chantierRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Chantier introuvable"
                            )
                    );

    chantierRepository.delete(chantier);
}

    /**
     * Transforme une entité Chantier en ChantierResponse.
     */

    private boolean estAdministrateurOuDirection() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        return false;
    }

    return authentication.getAuthorities()
            .stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                            || authority.getAuthority().equals("ROLE_DIRECTION")
            );
}


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