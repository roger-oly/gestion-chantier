package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Notification;
import com.gestionchantier.backend.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"
)
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    }

    /**
     * Retourne toutes les notifications d'un utilisateur.
     */
    @GetMapping("/utilisateur/{idUtilisateur}")
    public List<Notification> getNotificationsByUtilisateur(
            @PathVariable Integer idUtilisateur
    ) {
        return notificationService
                .getNotificationsByUtilisateur(idUtilisateur);
    }

    /**
     * Retourne les notifications non lues d'un utilisateur.
     */
    @GetMapping("/utilisateur/{idUtilisateur}/non-lues")
    public List<Notification> getNotificationsNonLues(
            @PathVariable Integer idUtilisateur
    ) {
        return notificationService
                .getNotificationsNonLues(idUtilisateur);
    }

    /**
     * Crée une notification.
     */
    @PostMapping
    public Notification createNotification(
            @RequestParam Integer idUtilisateur,
            @RequestParam String titre,
            @RequestParam String message,
            @RequestParam String type
    ) {
        return notificationService.createNotification(
                idUtilisateur,
                titre,
                message,
                type
        );
    }

    /**
     * Marque une notification comme lue.
     */
    @PutMapping("/{idNotification}/lue")
    public Notification marquerCommeLue(
            @PathVariable Integer idNotification
    ) {
        return notificationService
                .marquerCommeLue(idNotification);
    }

    /**
     * Supprime une notification.
     */
    @DeleteMapping("/{idNotification}")
    public void deleteNotification(
            @PathVariable Integer idNotification
    ) {
        notificationService
                .deleteNotification(idNotification);
    }
}