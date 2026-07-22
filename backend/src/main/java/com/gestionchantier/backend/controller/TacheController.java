package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Tache;
import com.gestionchantier.backend.service.TacheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping
    public List<Tache> getTaches() {
        return tacheService.getAllTaches();
    }

    @PostMapping
    public Tache saveTache(@RequestBody Tache tache) {
        return tacheService.saveTache(tache);
    }

    @PutMapping("/{id}")
    public Tache updateTache(
            @PathVariable Integer id,
            @RequestBody Tache tache) {

        return tacheService.updateTache(id, tache);
    }

    @DeleteMapping("/{id}")
    public void deleteTache(@PathVariable Integer id) {
        tacheService.deleteTache(id);
    }
}