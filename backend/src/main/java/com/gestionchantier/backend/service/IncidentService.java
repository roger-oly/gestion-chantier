package com.gestionchantier.backend.service;

import com.gestionchantier.backend.dto.IncidentRequest;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Incident;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.IncidentRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final ChantierRepository chantierRepository;
    private final UtilisateurRepository utilisateurRepository;

    public IncidentService(
            IncidentRepository incidentRepository,
            ChantierRepository chantierRepository,
            UtilisateurRepository utilisateurRepository) {

        this.incidentRepository = incidentRepository;
        this.chantierRepository = chantierRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Retourne tous les incidents.
     */
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    /**
     * Retourne les incidents d'un chantier.
     */
    public List<Incident> getIncidentsByChantier(Integer idChantier) {
        return incidentRepository.findByChantier_IdChantier(idChantier);
    }

    /**
     * Crée un nouvel incident.
     */
    public Incident saveIncident(IncidentRequest request) {

        if (request.getIdChantier() == null) {
            throw new IllegalArgumentException(
                    "Le chantier est obligatoire."
            );
        }

        if (request.getIdUtilisateur() == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur est obligatoire."
            );
        }

        Chantier chantier = chantierRepository
                .findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"
                        )
                );

        Utilisateur utilisateur = utilisateurRepository
                .findById(request.getIdUtilisateur())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        Incident incident = new Incident();

        incident.setType(request.getType());
        incident.setDescription(request.getDescription());
        incident.setGravite(request.getGravite());

        // Date réelle de l'incident
        incident.setDateIncident(request.getDateIncident());

        // Date de création / dernière modification
        incident.setDateModification(LocalDateTime.now());

        incident.setStatut(request.getStatut());
        incident.setChantier(chantier);
        incident.setUtilisateur(utilisateur);

        return incidentRepository.save(incident);
    }

    /**
     * Retourne un incident par son identifiant.
     */
    public Incident getIncidentById(Integer id) {

        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Incident introuvable"
                        )
                );
    }

    /**
     * Modifie un incident.
     */
    public Incident updateIncident(
            Integer id,
            Incident incident) {

        Incident existingIncident = incidentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Incident introuvable"
                        )
                );

        existingIncident.setType(incident.getType());
        existingIncident.setDescription(incident.getDescription());
        existingIncident.setGravite(incident.getGravite());
        existingIncident.setStatut(incident.getStatut());

        /*
         * La date réelle de l'incident ne change pas.
         *
         * On met uniquement à jour la date de modification.
         */
        existingIncident.setDateModification(
                LocalDateTime.now()
        );

        /*
         * Mise à jour du chantier.
         */
        if (incident.getChantier() != null
                && incident.getChantier().getIdChantier() != null) {

            Chantier chantier = chantierRepository
                    .findById(
                            incident.getChantier().getIdChantier()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Chantier introuvable"
                            )
                    );

            existingIncident.setChantier(chantier);
        }

        /*
         * Mise à jour de l'utilisateur.
         */
        if (incident.getUtilisateur() != null
                && incident.getUtilisateur().getIdUtilisateur() != null) {

            Utilisateur utilisateur = utilisateurRepository
                    .findById(
                            incident.getUtilisateur()
                                    .getIdUtilisateur()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Utilisateur introuvable"
                            )
                    );

            existingIncident.setUtilisateur(utilisateur);
        }

        return incidentRepository.save(existingIncident);
    }

    /**
     * Supprime un incident.
     */
    public void deleteIncident(Integer id) {

        incidentRepository.deleteById(id);
    }
}