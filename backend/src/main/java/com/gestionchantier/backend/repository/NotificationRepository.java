package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByUtilisateurIdUtilisateurOrderByDateCreationDesc(
            Integer idUtilisateur
    );

    List<Notification> findByUtilisateurIdUtilisateurAndLuFalseOrderByDateCreationDesc(
            Integer idUtilisateur
    );
}