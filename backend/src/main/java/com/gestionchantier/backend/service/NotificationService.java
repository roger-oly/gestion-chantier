package com.gestionchantier.backend.service;

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

        Notification notification = notificationRepository
                .findById(idNotification)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification introuvable"
                        )
                );

        notification.setLu(true);

        return notificationRepository.save(notification);
    }

    /**
     * Supprime une notification.
     */
    public void deleteNotification(
            Integer idNotification
    ) {

        Notification notification = notificationRepository
                .findById(idNotification)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification introuvable"
                        )
                );

        notificationRepository.delete(notification);
    }
}
