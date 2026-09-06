package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.gestionchantier.backend.entity.Notification;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.NotificationRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UtilisateurRepository utilisateurRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Retourne toutes les notifications d'un utilisateur.
     */
public List<Notification> getNotificationsByUtilisateur(
        Integer idUtilisateur
) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Utilisateur utilisateur = utilisateurRepository
            .findById(idUtilisateur)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Utilisateur introuvable"
                    )
            );

    /*
     * Un utilisateur ne peut consulter
     * que ses propres notifications.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter les notifications de cet utilisateur"
        );
    }

    return notificationRepository
            .findByUtilisateurIdUtilisateurOrderByDateCreationDesc(
                    idUtilisateur
            );
}

    /**
     * Retourne uniquement les notifications non lues.
     */
public List<Notification> getNotificationsNonLues(
        Integer idUtilisateur
) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Utilisateur utilisateur = utilisateurRepository
            .findById(idUtilisateur)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Utilisateur introuvable"
                    )
            );

    /*
     * Un utilisateur ne peut consulter
     * que ses propres notifications non lues.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à consulter les notifications de cet utilisateur"
        );
    }

    return notificationRepository
            .findByUtilisateurIdUtilisateurAndLuFalseOrderByDateCreationDesc(
                    idUtilisateur
            );
}

    /**
     * Crée une notification pour un utilisateur.
     */
    public Notification createNotification(
            Integer idUtilisateur,
            String titre,
            String message,
            String type
    ) {

        Utilisateur utilisateur = utilisateurRepository
                .findById(idUtilisateur)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        Notification notification = new Notification();

        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType(type);
        notification.setDateCreation(LocalDateTime.now());
        notification.setLu(false);
        notification.setUtilisateur(utilisateur);

        return notificationRepository.save(notification);
    }

    /**
     * Marque une notification comme lue.
     */
 public Notification marquerCommeLue(
        Integer idNotification
) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Notification notification = notificationRepository
            .findById(idNotification)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Notification introuvable"
                    )
            );

    /*
     * Une notification ne peut être marquée
     * comme lue que par son destinataire.
     */
    if (notification.getUtilisateur() == null
            || !notification.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à modifier cette notification"
        );
    }

    notification.setLu(true);

    return notificationRepository.save(notification);
}

    /**
     * Supprime une notification.
     */
public void deleteNotification(
        Integer idNotification
) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Notification notification = notificationRepository
            .findById(idNotification)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Notification introuvable"
                    )
            );

    /*
     * Une notification ne peut être supprimée
     * que par son destinataire.
     */
    if (notification.getUtilisateur() == null
            || !notification.getUtilisateur()
                    .getEmail()
                    .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à supprimer cette notification"
        );
    }

    notificationRepository.delete(notification);
}
}
