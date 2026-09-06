package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.gestionchantier.backend.dto.IncidentRequest;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Incident;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.IncidentRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final ChantierRepository chantierRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;

public IncidentService(
        IncidentRepository incidentRepository,
        ChantierRepository chantierRepository,
        UtilisateurRepository utilisateurRepository,
        NotificationService notificationService) {

    this.incidentRepository = incidentRepository;
    this.chantierRepository = chantierRepository;
    this.utilisateurRepository = utilisateurRepository;
    this.notificationService = notificationService;
}

    /**
     * Retourne tous les incidents.
     */
   public List<Incident> getAllIncidents() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                    );

    if (administrateurOuDirection) {
        return incidentRepository.findAll();
    }

    boolean peutVoirIncidents =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_RESPONSABLE_PROJET")
                                    || authority.getAuthority()
                                            .equals("ROLE_CHEF_CHANTIER")
                                    || authority.getAuthority()
                                            .equals("ROLE_OUVRIER")
                    );

    if (!peutVoirIncidents) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter les incidents"
        );
    }

    String emailUtilisateur =
            authentication.getName();

    return incidentRepository.findAll()
            .stream()
            .filter(incident ->
                    incident.getChantier() != null
                            && incident.getChantier()
                                    .getUtilisateur() != null
                            && emailUtilisateur.equals(
                                    incident.getChantier()
                                            .getUtilisateur()
                                            .getEmail()
                            )
            )
            .toList();
}

    /**
     * Retourne les incidents d'un chantier.
     */
   public List<Incident> getIncidentsByChantier(Integer idChantier) {

    Chantier chantier = chantierRepository
            .findById(idChantier)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Chantier introuvable"
                    ));

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                    );

    if (administrateurOuDirection) {
        return incidentRepository
                .findByChantier_IdChantier(idChantier);
    }

    boolean peutVoirIncidents =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_RESPONSABLE_PROJET")
                                    || authority.getAuthority()
                                            .equals("ROLE_CHEF_CHANTIER")
                                    || authority.getAuthority()
                                            .equals("ROLE_OUVRIER")
                    );

    if (!peutVoirIncidents) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter les incidents"
        );
    }

    if (chantier.getUtilisateur() == null
            || !chantier.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Accès interdit aux incidents de ce chantier"
        );
    }

    return incidentRepository
            .findByChantier_IdChantier(idChantier);
}

    /**
     * Crée un nouvel incident.
     */
  public Incident saveIncident(IncidentRequest request) {

    if (request.getIdChantier() == null) {
        throw new IllegalArgumentException(
                "Le chantier est obligatoire."
        );
    }

    if (request.getIdUtilisateur() == null) {
        throw new IllegalArgumentException(
                "L'utilisateur est obligatoire."
        );
    }

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                    );

    boolean peutCreerIncident =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                                    || authority.getAuthority()
                                            .equals("ROLE_RESPONSABLE_PROJET")
                                    || authority.getAuthority()
                                            .equals("ROLE_CHEF_CHANTIER")
                                    || authority.getAuthority()
                                            .equals("ROLE_OUVRIER")
                    );

    if (!peutCreerIncident) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à créer un incident"
        );
    }

    Chantier chantier = chantierRepository
            .findById(request.getIdChantier())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Chantier introuvable"
                    )
            );

    Utilisateur utilisateur = utilisateurRepository
            .findById(request.getIdUtilisateur())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Utilisateur introuvable"
                    )
            );

    /*
     * L'utilisateur enregistré comme auteur
     * doit être l'utilisateur actuellement connecté.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez pas créer un incident au nom d'un autre utilisateur"
        );
    }

    /*
     * ADMINISTRATEUR et DIRECTION peuvent créer
     * un incident sur n'importe quel chantier.
     *
     * Les autres rôles autorisés doivent appartenir
     * au chantier concerné.
     */
    if (!administrateurOuDirection) {

        if (chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas créer un incident sur ce chantier"
            );
        }
    }

    Incident incident = new Incident();

    incident.setType(request.getType());
    incident.setDescription(request.getDescription());
    incident.setGravite(request.getGravite());

    // Date réelle de l'incident
    incident.setDateIncident(request.getDateIncident());

    // Date de création / dernière modification
    incident.setDateModification(LocalDateTime.now());

    incident.setStatut(request.getStatut());
    incident.setChantier(chantier);
    incident.setUtilisateur(utilisateur);

    Incident savedIncident =
            incidentRepository.save(incident);

    if (chantier.getUtilisateur() != null
            && chantier.getUtilisateur().getIdUtilisateur() != null) {

        Integer idDestinataire =
                chantier.getUtilisateur().getIdUtilisateur();

        notificationService.createNotification(
                idDestinataire,
                "Nouvel incident",
                "Un nouvel incident a été enregistré sur le chantier "
                        + chantier.getNom(),
                "INCIDENT"
        );
    }

    return savedIncident;
}

    /**
     * Retourne un incident par son identifiant.
     */
  public Incident getIncidentById(Integer id) {

    Incident incident = incidentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Incident introuvable"
                    )
            );

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                    );

    if (administrateurOuDirection) {
        return incident;
    }

    boolean peutVoirIncident =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_RESPONSABLE_PROJET")
                                    || authority.getAuthority()
                                            .equals("ROLE_CHEF_CHANTIER")
                                    || authority.getAuthority()
                                            .equals("ROLE_OUVRIER")
                    );

    if (!peutVoirIncident) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter cet incident"
        );
    }

    if (incident.getChantier() == null
            || incident.getChantier().getUtilisateur() == null
            || !incident.getChantier()
                    .getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Accès interdit à cet incident"
        );
    }

    return incident;
}

    /**
     * Modifie un incident.
     */
public Incident updateIncident(
        Integer id,
        Incident incident) {

    Incident existingIncident = incidentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Incident introuvable"
                    )
            );

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                    );

    boolean peutModifierIncident =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                            .equals("ROLE_DIRECTION")
                                    || authority.getAuthority()
                                            .equals("ROLE_RESPONSABLE_PROJET")
                                    || authority.getAuthority()
                                            .equals("ROLE_CHEF_CHANTIER")
                    );

    if (!peutModifierIncident) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à modifier un incident"
        );
    }

    /*
     * ADMINISTRATEUR et DIRECTION peuvent modifier
     * les incidents de tous les chantiers.
     *
     * Les autres rôles autorisés doivent appartenir
     * au chantier de l'incident existant.
     */
    if (!administrateurOuDirection) {

        if (existingIncident.getChantier() == null
                || existingIncident.getChantier()
                        .getUtilisateur() == null
                || !existingIncident.getChantier()
                        .getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas modifier cet incident"
            );
        }
    }

    existingIncident.setType(incident.getType());
    existingIncident.setDescription(incident.getDescription());
    existingIncident.setGravite(incident.getGravite());
    existingIncident.setStatut(incident.getStatut());

    /*
     * La date réelle de l'incident ne change pas.
     *
     * On met uniquement à jour la date de modification.
     */
    existingIncident.setDateModification(
            LocalDateTime.now()
    );

    /*
     * Mise à jour du chantier.
     */
    if (incident.getChantier() != null
            && incident.getChantier().getIdChantier() != null) {

        Chantier chantier = chantierRepository
                .findById(
                        incident.getChantier().getIdChantier()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"
                        )
                );

        /*
         * Un utilisateur non ADMIN/DIRECTION
         * ne peut pas déplacer l'incident vers
         * un autre chantier qui ne lui appartient pas.
         */
        if (!administrateurOuDirection) {

            if (chantier.getUtilisateur() == null
                    || !chantier.getUtilisateur()
                            .getEmail()
                            .equals(authentication.getName())) {

                throw new AccessDeniedException(
                        "Vous ne pouvez pas affecter cet incident à ce chantier"
                );
            }
        }

        existingIncident.setChantier(chantier);
    }

    /*
     * L'utilisateur ayant créé l'incident
     * reste inchangé.
     *
     * On ne permet pas de modifier l'auteur
     * depuis cette opération.
     */

    return incidentRepository.save(existingIncident);
}

    /**
     * Supprime un incident.
     */
public void deleteIncident(Integer id) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Incident incident = incidentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Incident introuvable"
                    )
            );

    boolean administrateur =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                    );

    boolean direction =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_DIRECTION")
                    );

    if (!administrateur && !direction) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à supprimer un incident"
        );
    }

    if (direction) {

        String emailUtilisateur =
                authentication.getName();

        if (incident.getChantier() == null
                || incident.getChantier().getUtilisateur() == null
                || !incident.getChantier()
                        .getUtilisateur()
                        .getEmail()
                        .equals(emailUtilisateur)) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à supprimer cet incident"
            );
        }
    }

    incidentRepository.delete(incident);
}
}