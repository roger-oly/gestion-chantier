package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Livraison;
import com.gestionchantier.backend.repository.LivraisonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;

    public LivraisonService(LivraisonRepository livraisonRepository) {
        this.livraisonRepository = livraisonRepository;
    }

    /**
     * Retourne toutes les livraisons.
     */
    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    /**
     * Enregistre une nouvelle livraison.
     */
    public Livraison saveLivraison(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    /**
     * Met à jour une livraison.
     */
    public Livraison updateLivraison(Integer id, Livraison livraison) {

        Livraison existingLivraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable"));

        existingLivraison.setDescription(livraison.getDescription());
        existingLivraison.setDateLivraison(livraison.getDateLivraison());
        existingLivraison.setStatut(livraison.getStatut());
        existingLivraison.setChantier(livraison.getChantier());
        existingLivraison.setUtilisateur(livraison.getUtilisateur());

        return livraisonRepository.save(existingLivraison);
    }

    /**
     * Supprime une livraison.
     */
    public void deleteLivraison(Integer id) {

        livraisonRepository.deleteById(id);
    }
}
