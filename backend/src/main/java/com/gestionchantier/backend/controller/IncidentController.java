package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Incident;
import com.gestionchantier.backend.service.IncidentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<Incident> getIncidents() {
        return incidentService.getAllIncidents();
    }

    @PostMapping
    public Incident saveIncident(@RequestBody Incident incident) {
        return incidentService.saveIncident(incident);
    }

    @PutMapping("/{id}")
    public Incident updateIncident(
            @PathVariable Integer id,
            @RequestBody Incident incident) {

        return incidentService.updateIncident(id, incident);
    }

    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable Integer id) {
        incidentService.deleteIncident(id);
    }
}
