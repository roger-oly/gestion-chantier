package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.gestionchantier.backend.entity.Avancement;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Tache;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.AvancementRepository;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.TacheRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvancementService {

    private final AvancementRepository avancementRepository;
    private final ChantierRepository chantierRepository;
    private final TacheRepository tacheRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AvancementService(
            AvancementRepository avancementRepository,
            ChantierRepository chantierRepository,
            TacheRepository tacheRepository,
            UtilisateurRepository utilisateurRepository) {

        this.avancementRepository = avancementRepository;
        this.chantierRepository = chantierRepository;
        this.tacheRepository = tacheRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Retourne tous les avancements.
     */
   public List<Avancement> getAllAvancements() {

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
        return avancementRepository.findAll();
    }

    String emailUtilisateur =
            authentication.getName();

    return avancementRepository.findAll()
            .stream()
            .filter(avancement ->
                    avancement.getChantier() != null
                            && avancement.getChantier()
                                    .getUtilisateur() != null
                            && emailUtilisateur.equals(
                                    avancement.getChantier()
                                            .getUtilisateur()
                                            .getEmail()
                            )
            )
            .toList();
}

    /**
     * Retourne l'historique des avancements d'un chantier.
     */
   public List<Avancement> getAvancementsByChantier(Integer idChantier) {

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

        if (authentication == null
                || chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Accès interdit à l'avancement de ce chantier"
            );
        }
    }

    return avancementRepository
            .findByChantier_IdChantierOrderByDateMiseAJourDesc(
                    idChantier
            );
}

    /**
     * Calcule l'avancement actuel d'un chantier
     * à partir des tâches terminées.
     */
public int calculerPourcentage(Integer idChantier) {

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

    if (!administrateurOuDirection) {

        if (chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Accès interdit à l'avancement de ce chantier"
            );
        }
    }

    List<Tache> taches =
            tacheRepository.findByChantier_IdChantier(idChantier);

    if (taches.isEmpty()) {
        return 0;
    }

    long tachesTerminees = taches.stream()
            .filter(tache -> {
                String statut = tache.getStatut();

                return "Terminé".equalsIgnoreCase(statut)
                        || "Terminée".equalsIgnoreCase(statut);
            })
            .count();

    return (int) Math.round(
            (tachesTerminees * 100.0) / taches.size()
    );
}

    /**
     * Enregistre une validation d'avancement.
     *
     * Le pourcentage est calculé automatiquement
     * à partir des tâches terminées.
     */
   public Avancement saveAvancement(
        Integer idChantier,
        Integer idUtilisateur,
        String commentaire) {

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

    boolean peutValider =
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

    if (!peutValider) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à valider un avancement"
        );
    }

    Chantier chantier = chantierRepository
            .findById(idChantier)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Chantier introuvable"
                    ));

    Utilisateur utilisateur = utilisateurRepository
            .findById(idUtilisateur)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Utilisateur introuvable"
                    ));

    /*
     * L'utilisateur enregistré comme validateur
     * doit être l'utilisateur actuellement connecté.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez pas valider un avancement au nom d'un autre utilisateur"
        );
    }

    /*
     * ADMINISTRATEUR et DIRECTION peuvent valider
     * l'avancement de n'importe quel chantier.
     *
     * Les autres rôles autorisés doivent être
     * responsables du chantier concerné.
     */
    if (!administrateurOuDirection) {

        if (chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas valider l'avancement de ce chantier"
            );
        }
    }

    int pourcentage =
            calculerPourcentage(idChantier);

    Avancement avancement = Avancement.builder()
            .pourcentage(pourcentage)
            .commentaire(commentaire)
            .dateMiseAJour(LocalDateTime.now())
            .chantier(chantier)
            .utilisateur(utilisateur)
            .build();

    return avancementRepository.save(avancement);
}

    /**
     * Supprime un avancement.
     */
   public void deleteAvancement(Integer id) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateur =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                    );

    if (!administrateur) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à supprimer un avancement"
        );
    }

    Avancement avancement =
            avancementRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Avancement introuvable"
                            ));

    avancementRepository.delete(avancement);
}
}