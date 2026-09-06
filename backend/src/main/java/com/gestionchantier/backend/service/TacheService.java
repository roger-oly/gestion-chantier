package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.gestionchantier.backend.dto.TacheRequest;
import com.gestionchantier.backend.dto.TacheResponse;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Tache;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.TacheRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;
    private final ChantierRepository chantierRepository;
    private final AvancementService avancementService;
    private final NotificationService notificationService;

    public TacheService(
            TacheRepository tacheRepository,
            ChantierRepository chantierRepository,
            AvancementService avancementService,
            NotificationService notificationService) {

        this.tacheRepository = tacheRepository;
        this.chantierRepository = chantierRepository;
        this.avancementService = avancementService;
        this.notificationService = notificationService;
    }

    /**
     * Retourne toutes les tâches.
     */
  public List<TacheResponse> getAllTaches() {

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

        return tacheRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    String emailUtilisateur =
            authentication.getName();

    return tacheRepository.findAll()
            .stream()
            .filter(tache ->
                    tache.getChantier() != null
                            && tache.getChantier()
                                    .getUtilisateur() != null
                            && emailUtilisateur.equals(
                                    tache.getChantier()
                                            .getUtilisateur()
                                            .getEmail()
                            )
            )
            .map(this::toResponse)
            .toList();
}

    /**
     * Retourne les tâches associées à un chantier.
     */
  public List<TacheResponse> getTachesByChantier(Integer idChantier) {

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

    /*
     * Administrateur et Direction :
     * accès aux tâches de tous les chantiers.
     */
    boolean administrateurOuDirection =
            authentication != null
                    && authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                    .equals("ROLE_DIRECTION")
                    );

    /*
     * Les autres utilisateurs ne peuvent consulter
     * que les tâches de leurs propres chantiers.
     */
    if (!administrateurOuDirection) {

        if (authentication == null
                || chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Accès interdit aux tâches de ce chantier"
            );
        }
    }

    return tacheRepository
            .findByChantier_IdChantier(idChantier)
            .stream()
            .map(this::toResponse)
            .toList();
}

    /**
     * Retourne une tâche par son identifiant.
     */
  public TacheResponse getTacheById(Integer id) {

    Tache tache = tacheRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Tache introuvable"));

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    boolean administrateurOuDirection =
            authentication != null
                    && authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                    .equals("ROLE_DIRECTION")
                    );

    if (!administrateurOuDirection) {

        Chantier chantier = tache.getChantier();

        if (authentication == null
                || chantier == null
                || chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Accès interdit à cette tâche"
            );
        }
    }

    return toResponse(tache);
}

    /**
     * Enregistre une nouvelle tâche.
     */
public TacheResponse saveTache(TacheRequest request) {

    Chantier chantier = chantierRepository
            .findById(request.getIdChantier())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Chantier introuvable"));

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();
                    if (authentication == null
        || authentication.getAuthorities()
                .stream()
                .noneMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                                || authority.getAuthority().equals("ROLE_DIRECTION")
                                || authority.getAuthority().equals("ROLE_RESPONSABLE_PROJET")
                                || authority.getAuthority().equals("ROLE_CHEF_CHANTIER"))) {

    throw new AccessDeniedException(
            "Vous n'êtes pas autorisé à créer une tâche"
    );
}

    boolean administrateurOuDirection =
            authentication != null
                    && authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                                    || authority.getAuthority()
                                    .equals("ROLE_DIRECTION")
                    );

    /*
     * Administrateur et Direction :
     * peuvent créer une tâche sur n'importe quel chantier.
     */
    if (!administrateurOuDirection) {

        if (authentication == null
                || chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à créer une tâche sur ce chantier"
            );
        }
    }

    Tache tache = new Tache();

    tache.setTitre(request.getTitre());
    tache.setDescription(request.getDescription());
    tache.setStatut(request.getStatut());
    tache.setNiveauPriorite(request.getNiveauPriorite());
    tache.setChantier(chantier);

    Tache savedTache =
            tacheRepository.save(tache);

    return toResponse(savedTache);
}

    /**
     * Met à jour une tâche.
     *
     * Si la modification entraîne un changement
     * du pourcentage d'avancement du chantier,
     * une notification est envoyée au responsable
     * du chantier.
     */
    public TacheResponse updateTache(
            Integer id,
            TacheRequest request) {

        Tache existingTache = tacheRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tache introuvable"));

        Authentication authentication =
        SecurityContextHolder
                .getContext()
                .getAuthentication();

boolean administrateurOuDirection =
        authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMINISTRATEUR")
                                || authority.getAuthority()
                                        .equals("ROLE_DIRECTION")
                );

                boolean peutModifierTache =
        authentication != null
                && authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                                || authority.getAuthority().equals("ROLE_DIRECTION")
                                || authority.getAuthority().equals("ROLE_RESPONSABLE_PROJET")
                                || authority.getAuthority().equals("ROLE_CHEF_CHANTIER")
                );

if (!peutModifierTache) {

    throw new AccessDeniedException(
            "Vous n'êtes pas autorisé à modifier une tâche"
    );
}

if (!administrateurOuDirection) {

    Chantier chantierActuel =
            existingTache.getChantier();

    if (authentication == null
            || chantierActuel == null
            || chantierActuel.getUtilisateur() == null
            || !chantierActuel.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à modifier cette tâche"
        );
    }
}

        /*
         * Chantier avant modification.
         */
        Chantier ancienChantier = existingTache.getChantier();

        Integer ancienIdChantier =
                ancienChantier.getIdChantier();

        /*
         * Avancement avant modification.
         */
        int ancienPourcentage =
                avancementService.calculerPourcentage(
                        ancienIdChantier
                );

        /*
         * Nouveau chantier.
         */
        Chantier nouveauChantier = chantierRepository
                .findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"));
        
        if (!administrateurOuDirection) {

    if (nouveauChantier.getUtilisateur() == null
            || authentication == null
            || !nouveauChantier.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à affecter cette tâche à ce chantier"
        );
    }
}

        /*
         * Modification de la tâche.
         */
        existingTache.setTitre(request.getTitre());
        existingTache.setDescription(request.getDescription());
        existingTache.setStatut(request.getStatut());
        existingTache.setNiveauPriorite(request.getNiveauPriorite());
        existingTache.setChantier(nouveauChantier);

        /*
         * Enregistrement de la modification.
         */
        Tache updatedTache =
                tacheRepository.save(existingTache);

        /*
         * Avancement après modification.
         */
        int nouveauPourcentage =
                avancementService.calculerPourcentage(
                        nouveauChantier.getIdChantier()
                );

        /*
         * Notification uniquement si l'avancement
         * du chantier a réellement changé.
         */
        if (ancienPourcentage != nouveauPourcentage
                && nouveauChantier.getUtilisateur() != null
                && nouveauChantier.getUtilisateur()
                        .getIdUtilisateur() != null) {

            Integer idDestinataire =
                    nouveauChantier.getUtilisateur()
                            .getIdUtilisateur();

            notificationService.createNotification(
                    idDestinataire,
                    "Mise à jour de l'avancement",
                    "Le chantier \""
                            + nouveauChantier.getNom()
                            + "\" est maintenant à "
                            + nouveauPourcentage
                            + "%. Veuillez valider cette mise à jour.",
                    "AVANCEMENT"
            );
        }

        return toResponse(updatedTache);
    }

    /**
     * Supprime une tâche.
     */
public void deleteTache(Integer id) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Tache tache = tacheRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Tache introuvable"
                    ));

    boolean administrateur =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                    );

    boolean responsableProjet =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_RESPONSABLE_PROJET")
                    );

    boolean chefChantier =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_CHEF_CHANTIER")
                    );

    if (!administrateur && !responsableProjet && !chefChantier) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à supprimer une tâche"
        );
    }

    if (responsableProjet || chefChantier) {

        String emailUtilisateur =
                authentication.getName();

        if (tache.getChantier() == null
                || tache.getChantier().getUtilisateur() == null
                || !tache.getChantier()
                        .getUtilisateur()
                        .getEmail()
                        .equals(emailUtilisateur)) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à supprimer cette tâche"
            );
        }
    }

    tacheRepository.delete(tache);
}

    /**
     * Transforme une entité Tache en TacheResponse.
     */
    private TacheResponse toResponse(Tache tache) {

        return TacheResponse.builder()
                .idTache(tache.getIdTache())
                .titre(tache.getTitre())
                .description(tache.getDescription())
                .statut(tache.getStatut())
                .niveauPriorite(tache.getNiveauPriorite())
                .idChantier(
                        tache.getChantier().getIdChantier()
                )
                .nomChantier(
                        tache.getChantier().getNom()
                )
                .build();
    }
}