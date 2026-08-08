package com.gestionchantier.backend.service;

import com.gestionchantier.backend.dto.TacheRequest;
import com.gestionchantier.backend.dto.TacheResponse;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Tache;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.TacheRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacheService {

    private final TacheRepository tacheRepository;
    private final ChantierRepository chantierRepository;

    public TacheService(
            TacheRepository tacheRepository,
            ChantierRepository chantierRepository) {

        this.tacheRepository = tacheRepository;
        this.chantierRepository = chantierRepository;
    }

    /**
     * Retourne toutes les tâches.
     */
    public List<TacheResponse> getAllTaches() {

        return tacheRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
 * Retourne les tâches associées à un chantier.
 */
public List<TacheResponse> getTachesByChantier(Integer idChantier) {

    return tacheRepository
            .findByChantier_IdChantier(idChantier)
            .stream()
            .map(this::toResponse)
            .toList();
}

    /**
     * Retourne les tâches par l'id.
     */

    public TacheResponse getTacheById(Integer id) {

    Tache tache = tacheRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Tache introuvable"));

    return toResponse(tache);


}

    /**
     * Enregistre une nouvelle tâche.
     */
    public TacheResponse saveTache(TacheRequest request) {

        Chantier chantier = chantierRepository.findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chantier introuvable"));

        Tache tache = new Tache();

        tache.setTitre(request.getTitre());
        tache.setDescription(request.getDescription());
        tache.setStatut(request.getStatut());
        tache.setNiveauPriorite(request.getNiveauPriorite());
        tache.setChantier(chantier);

        Tache savedTache = tacheRepository.save(tache);

        return toResponse(savedTache);
    }

    /**
     * Met à jour une tâche.
     */
    public TacheResponse updateTache(
            Integer id,
            TacheRequest request) {

        Tache existingTache = tacheRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tache introuvable"));

        Chantier chantier = chantierRepository.findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chantier introuvable"));

        existingTache.setTitre(request.getTitre());
        existingTache.setDescription(request.getDescription());
        existingTache.setStatut(request.getStatut());
        existingTache.setNiveauPriorite(request.getNiveauPriorite());
        existingTache.setChantier(chantier);

        Tache updatedTache = tacheRepository.save(existingTache);

        return toResponse(updatedTache);
    }

    /**
     * Supprime une tâche.
     */
    public void deleteTache(Integer id) {

        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tache introuvable"));

        tacheRepository.delete(tache);
    }

    /**
     * Transforme une entité Tache en TacheResponse.
     */
    private TacheResponse toResponse(Tache tache) {

        return TacheResponse.builder()
                .idTache(tache.getIdTache())
                .titre(tache.getTitre())
                .description(tache.getDescription())
                .statut(tache.getStatut())
                .niveauPriorite(tache.getNiveauPriorite())
                .idChantier(tache.getChantier().getIdChantier())
                .nomChantier(tache.getChantier().getNom())
                .build();
    }
}