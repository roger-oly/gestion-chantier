package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return livraisonRepository.findAll();
    }

    boolean peutVoirLivraisons =
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

    if (!peutVoirLivraisons) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter les livraisons"
        );
    }

    String emailUtilisateur =
            authentication.getName();

    return livraisonRepository.findAll()
            .stream()
            .filter(livraison ->
                    livraison.getChantier() != null
                            && livraison.getChantier()
                                    .getUtilisateur() != null
                            && emailUtilisateur.equals(
                                    livraison.getChantier()
                                            .getUtilisateur()
                                            .getEmail()
                            )
            )
            .toList();
}

    /**
     * Récupère une livraison par son identifiant.
     */
    public Livraison getLivraisonById(Integer id) {

    Livraison livraison = livraisonRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Livraison introuvable"));

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
        return livraison;
    }

    boolean peutVoirLivraison =
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

    if (!peutVoirLivraison) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter cette livraison"
        );
    }

    if (livraison.getChantier() == null
            || livraison.getChantier()
                    .getUtilisateur() == null
            || !livraison.getChantier()
                    .getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Accès interdit à cette livraison"
        );
    }

    return livraison;
}

    /**
     * Récupère les livraisons d'un chantier.
     */
  public List<Livraison> getLivraisonsByChantier(Integer idChantier) {

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
        return livraisonRepository
                .findByChantier_IdChantier(idChantier);
    }

    boolean peutVoirLivraisons =
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

    if (!peutVoirLivraisons) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter les livraisons"
        );
    }

    if (chantier.getUtilisateur() == null
            || !chantier.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Accès interdit aux livraisons de ce chantier"
        );
    }

    return livraisonRepository
            .findByChantier_IdChantier(idChantier);
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

    boolean peutCreerLivraison =
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
                                            .equals("ROLE_FOURNISSEUR")
                    );

    if (!peutCreerLivraison) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à créer une livraison"
        );
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

    /*
     * L'utilisateur enregistré sur la livraison
     * doit être l'utilisateur actuellement connecté.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez pas créer une livraison au nom d'un autre utilisateur"
        );
    }

    /*
     * ADMINISTRATEUR et DIRECTION peuvent créer
     * une livraison sur n'importe quel chantier.
     *
     * Les autres rôles autorisés doivent être
     * rattachés au chantier concerné.
     */
    if (!administrateurOuDirection) {

        /*
         * Le FOURNISSEUR constitue une exception :
         * il peut créer une livraison pour un chantier
         * sans être le responsable du chantier.
         */
        boolean fournisseur =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_FOURNISSEUR")
                        );

        if (!fournisseur) {

            if (chantier.getUtilisateur() == null
                    || !chantier.getUtilisateur()
                            .getEmail()
                            .equals(authentication.getName())) {

                throw new AccessDeniedException(
                        "Vous ne pouvez pas créer une livraison sur ce chantier"
                );
            }
        }
    }

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
     * Notification du responsable du chantier.
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

    boolean peutModifierLivraison =
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

    if (!peutModifierLivraison) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à modifier une livraison"
        );
    }

    /*
     * ADMINISTRATEUR et DIRECTION peuvent modifier
     * les livraisons de tous les chantiers.
     *
     * Les autres rôles autorisés doivent appartenir
     * au chantier actuel de la livraison.
     */
    if (!administrateurOuDirection) {

        if (existingLivraison.getChantier() == null
                || existingLivraison.getChantier()
                        .getUtilisateur() == null
                || !existingLivraison.getChantier()
                        .getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas modifier cette livraison"
            );
        }
    }

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

    /*
     * L'utilisateur enregistré doit rester
     * l'utilisateur connecté.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez pas modifier la livraison au nom d'un autre utilisateur"
        );
    }

    /*
     * Un utilisateur non ADMIN/DIRECTION ne peut pas
     * déplacer la livraison vers un autre chantier
     * qui ne lui appartient pas.
     *
     * Le FOURNISSEUR ne peut pas modifier une livraison.
     */
    if (!administrateurOuDirection) {

        if (chantier.getUtilisateur() == null
                || !chantier.getUtilisateur()
                        .getEmail()
                        .equals(authentication.getName())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas affecter cette livraison à ce chantier"
            );
        }
    }

    existingLivraison.setDescription(
            request.getDescription()
    );

    existingLivraison.setDateLivraison(
            request.getDateLivraison()
    );

    existingLivraison.setStatut(
            request.getStatut()
    );

    existingLivraison.setChantier(chantier);

    existingLivraison.setUtilisateur(utilisateur);

    return livraisonRepository.save(existingLivraison);
}

    /**
     * Supprime une livraison.
     */
   public void deleteLivraison(Integer id) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Livraison livraison = livraisonRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Livraison introuvable"
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
                "Vous n'êtes pas autorisé à supprimer une livraison"
        );
    }

    if (direction) {

        String emailUtilisateur =
                authentication.getName();

        if (livraison.getChantier() == null
                || livraison.getChantier().getUtilisateur() == null
                || !livraison.getChantier()
                        .getUtilisateur()
                        .getEmail()
                        .equals(emailUtilisateur)) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à supprimer cette livraison"
            );
        }
    }

    livraisonRepository.delete(livraison);
}
}