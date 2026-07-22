package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.repository.ChantierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChantierService {

    private final ChantierRepository chantierRepository;

    public ChantierService(ChantierRepository chantierRepository) {
        this.chantierRepository = chantierRepository;
    }

    /**
     * Retourne tous les chantiers.
     */
    public List<Chantier> getAllChantiers() {
        return chantierRepository.findAll();
    }

    /**
     * Enregistre un nouveau chantier.
     */
    public Chantier saveChantier(Chantier chantier) {
        return chantierRepository.save(chantier);
    }

    /**
     * Met à jour un chantier.
     */
    public Chantier updateChantier(Integer id, Chantier chantier) {

        Chantier existingChantier = chantierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chantier introuvable"));

        existingChantier.setNom(chantier.getNom());
        existingChantier.setDescription(chantier.getDescription());
        existingChantier.setLocalisation(chantier.getLocalisation());
        existingChantier.setBudget(chantier.getBudget());
        existingChantier.setDateDebut(chantier.getDateDebut());
        existingChantier.setDateFinPrevue(chantier.getDateFinPrevue());
        existingChantier.setStatut(chantier.getStatut());
        existingChantier.setUtilisateur(chantier.getUtilisateur());

        return chantierRepository.save(existingChantier);
    }

    /**
     * Supprime un chantier.
     */
    public void deleteChantier(Integer id) {
        chantierRepository.deleteById(id);
    }
}