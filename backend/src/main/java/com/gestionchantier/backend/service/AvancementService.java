package com.gestionchantier.backend.service;

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
        return avancementRepository.findAll();
    }

    /**
     * Retourne l'historique des avancements d'un chantier.
     */
    public List<Avancement> getAvancementsByChantier(Integer idChantier) {

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

        Avancement avancement =
                avancementRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Avancement introuvable"
                                ));

        avancementRepository.delete(avancement);
    }
}