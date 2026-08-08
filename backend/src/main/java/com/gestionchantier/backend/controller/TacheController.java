package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.dto.TacheRequest;
import com.gestionchantier.backend.dto.TacheResponse;
import com.gestionchantier.backend.service.TacheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"
)
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }

    @GetMapping
    public List<TacheResponse> getTaches() {
        return tacheService.getAllTaches();
    }

    @GetMapping("/{id}")
public TacheResponse getTacheById(
        @PathVariable Integer id) {

    return tacheService.getTacheById(id);
}

    @GetMapping("/chantier/{idChantier}")
public List<TacheResponse> getTachesByChantier(
        @PathVariable Integer idChantier) {

    return tacheService.getTachesByChantier(idChantier);
}

    @PostMapping
    public TacheResponse saveTache(
            @RequestBody TacheRequest request) {

        return tacheService.saveTache(request);
    }

    @PutMapping("/{id}")
    public TacheResponse updateTache(
            @PathVariable Integer id,
            @RequestBody TacheRequest request) {

        return tacheService.updateTache(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTache(
            @PathVariable Integer id) {

        tacheService.deleteTache(id);
    }
}