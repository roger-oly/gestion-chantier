package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    private final PasswordEncoder passwordEncoder;
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(
        UtilisateurRepository utilisateurRepository,
        PasswordEncoder passwordEncoder) {

    this.utilisateurRepository = utilisateurRepository;
    this.passwordEncoder = passwordEncoder;
}

    /**
     * Retourne tous les utilisateurs.
     */
   public List<Utilisateur> getAllUtilisateurs() {

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
                "Vous n'êtes pas autorisé à consulter la liste des utilisateurs"
        );
    }

    return utilisateurRepository.findAll();
}

public Utilisateur getUtilisateurById(Integer id) {

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
                "Vous n'êtes pas autorisé à consulter cet utilisateur"
        );
    }

    return utilisateurRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Utilisateur introuvable"
                    )
            );
}
    /**
     * Enregistre un nouvel utilisateur.
     */
 public Utilisateur saveUtilisateur(Utilisateur utilisateur) {

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
                "Vous n'êtes pas autorisé à créer un utilisateur"
        );
    }

    utilisateur.setMotDePasse(
            passwordEncoder.encode(
                    utilisateur.getMotDePasse()
            )
    );

    return utilisateurRepository.save(utilisateur);
}

    /**
     * Met à jour un utilisateur.
     */
public Utilisateur updateUtilisateur(
        Integer id,
        Utilisateur utilisateur) {

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
                "Vous n'êtes pas autorisé à modifier cet utilisateur"
        );
    }

    Utilisateur existingUtilisateur =
            utilisateurRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Utilisateur introuvable"
                            )
                    );

    existingUtilisateur.setNom(utilisateur.getNom());
    existingUtilisateur.setPrenom(utilisateur.getPrenom());
    existingUtilisateur.setEmail(utilisateur.getEmail());

    if (utilisateur.getMotDePasse() != null
            && !utilisateur.getMotDePasse().isBlank()) {

        existingUtilisateur.setMotDePasse(
                passwordEncoder.encode(
                        utilisateur.getMotDePasse()
                )
        );
    }

    existingUtilisateur.setTelephone(
            utilisateur.getTelephone()
    );

    existingUtilisateur.setStatut(
            utilisateur.getStatut()
    );

    existingUtilisateur.setRole(
            utilisateur.getRole()
    );

    return utilisateurRepository.save(existingUtilisateur);
}
    /**
     * Supprime un utilisateur.
     */
public void deleteUtilisateur(Integer id) {

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
                "Vous n'êtes pas autorisé à supprimer un utilisateur"
        );
    }

    Utilisateur utilisateur =
            utilisateurRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Utilisateur introuvable"
                            )
                    );

    utilisateurRepository.delete(utilisateur);
}

public void changePassword(
        Integer id,
        String ancienMotDePasse,
        String nouveauMotDePasse) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Utilisateur utilisateur =
            utilisateurRepository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Utilisateur introuvable"
                            )
                    );

    /*
     * L'utilisateur ne peut modifier
     * que son propre mot de passe.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez modifier que votre propre mot de passe"
        );
    }

    if (!passwordEncoder.matches(
            ancienMotDePasse,
            utilisateur.getMotDePasse())) {

        throw new IllegalArgumentException(
                "Ancien mot de passe incorrect"
        );
    }

    utilisateur.setMotDePasse(
            passwordEncoder.encode(
                    nouveauMotDePasse
            )
    );

    utilisateurRepository.save(utilisateur);
}
}