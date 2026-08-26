package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.dto.IncidentRequest;
import com.gestionchantier.backend.entity.Incident;
import com.gestionchantier.backend.service.IncidentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"
)
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

    @GetMapping("/{id}")
public Incident getIncidentById(@PathVariable Integer id) {
    return incidentService.getIncidentById(id);
}

@GetMapping("/chantier/{idChantier}")
public List<Incident> getIncidentsByChantier(
        @PathVariable Integer idChantier) {

    return incidentService.getIncidentsByChantier(idChantier);
}

   @PostMapping
public Incident saveIncident(@RequestBody IncidentRequest request) {
    return incidentService.saveIncident(request);
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
