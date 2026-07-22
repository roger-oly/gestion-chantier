package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Incident;
import com.gestionchantier.backend.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    /**
     * Retourne tous les incidents.
     */
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    /**
     * Enregistre un nouvel incident.
     */
    public Incident saveIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    /**
     * Met à jour un incident.
     */
    public Incident updateIncident(Integer id, Incident incident) {

        Incident existingIncident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident introuvable"));

        existingIncident.setType(incident.getType());
        existingIncident.setDescription(incident.getDescription());
        existingIncident.setGravite(incident.getGravite());
        existingIncident.setDateIncident(incident.getDateIncident());
        existingIncident.setStatut(incident.getStatut());
        existingIncident.setChantier(incident.getChantier());
        existingIncident.setUtilisateur(incident.getUtilisateur());

        return incidentRepository.save(existingIncident);
    }

    /**
     * Supprime un incident.
     */
    public void deleteIncident(Integer id) {

        incidentRepository.deleteById(id);
    }
}
