package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Avancement;
import com.gestionchantier.backend.repository.AvancementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvancementService {

    private final AvancementRepository avancementRepository;

    public AvancementService(AvancementRepository avancementRepository) {
        this.avancementRepository = avancementRepository;
    }

    /**
     * Retourne tous les avancements.
     */
    public List<Avancement> getAllAvancements() {
        return avancementRepository.findAll();
    }

    /**
     * Enregistre un nouvel avancement.
     */
    public Avancement saveAvancement(Avancement avancement) {
        return avancementRepository.save(avancement);
    }

    /**
     * Met à jour un avancement.
     */
    public Avancement updateAvancement(Integer id, Avancement avancement) {

        Avancement existingAvancement = avancementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avancement introuvable"));

        existingAvancement.setPourcentage(avancement.getPourcentage());
        existingAvancement.setCommentaire(avancement.getCommentaire());
        existingAvancement.setDateMiseAJour(avancement.getDateMiseAJour());
        existingAvancement.setTache(avancement.getTache());
        existingAvancement.setUtilisateur(avancement.getUtilisateur());

        return avancementRepository.save(existingAvancement);
    }

    /**
     * Supprime un avancement.
     */
    public void deleteAvancement(Integer id) {

        avancementRepository.deleteById(id);
    }
}