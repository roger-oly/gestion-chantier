package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Tache;
import com.gestionchantier.backend.repository.TacheRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;

    public TacheService(TacheRepository tacheRepository) {
        this.tacheRepository = tacheRepository;
    }

    /**
     * Retourne toutes les tâches.
     */
    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    /**
     * Enregistre une nouvelle tâche.
     */
    public Tache saveTache(Tache tache) {
        return tacheRepository.save(tache);
    }

    /**
     * Met à jour une tâche.
     */
    public Tache updateTache(Integer id, Tache tache) {

        Tache existingTache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tache introuvable"));

        existingTache.setTitre(tache.getTitre());
        existingTache.setDescription(tache.getDescription());
        existingTache.setStatut(tache.getStatut());
        existingTache.setNiveauPriorite(tache.getNiveauPriorite());
        existingTache.setChantier(tache.getChantier());

        return tacheRepository.save(existingTache);
    }

    /**
     * Supprime une tâche.
     */
    public void deleteTache(Integer id) {

        tacheRepository.deleteById(id);
    }
}
