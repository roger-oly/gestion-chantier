package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Livraison;
import com.gestionchantier.backend.service.LivraisonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    private final LivraisonService livraisonService;

    public LivraisonController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    @GetMapping
    public List<Livraison> getLivraisons() {
        return livraisonService.getAllLivraisons();
    }

    @PostMapping
    public Livraison saveLivraison(@RequestBody Livraison livraison) {
        return livraisonService.saveLivraison(livraison);
    }

    @PutMapping("/{id}")
    public Livraison updateLivraison(
            @PathVariable Integer id,
            @RequestBody Livraison livraison) {

        return livraisonService.updateLivraison(id, livraison);
    }

    @DeleteMapping("/{id}")
    public void deleteLivraison(@PathVariable Integer id) {
        livraisonService.deleteLivraison(id);
    }
}