package com.gestionchantier.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import com.gestionchantier.backend.dto.LivraisonRequest;
import com.gestionchantier.backend.entity.Livraison;
import com.gestionchantier.backend.service.LivraisonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev")
@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    private final LivraisonService livraisonService;

    public LivraisonController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    /**
     * Récupère toutes les livraisons.
     */
    @GetMapping
    public List<Livraison> getLivraisons() {
        return livraisonService.getAllLivraisons();
    }

    /**
     * Récupère une livraison par son identifiant.
     */
    @GetMapping("/{id}")
    public Livraison getLivraisonById(@PathVariable Integer id) {
        return livraisonService.getLivraisonById(id);
    }

    /**
     * Récupère les livraisons d'un chantier.
     */
    @GetMapping("/chantier/{idChantier}")
    public List<Livraison> getLivraisonsByChantier(
            @PathVariable Integer idChantier) {

        return livraisonService.getLivraisonsByChantier(idChantier);
    }

    /**
     * Crée une livraison.
     */
    @PostMapping
    public Livraison saveLivraison(
            @RequestBody LivraisonRequest request) {

        return livraisonService.saveLivraison(request);
    }

    /**
     * Modifie une livraison.
     */
    @PutMapping("/{id}")
    public Livraison updateLivraison(
            @PathVariable Integer id,
            @RequestBody LivraisonRequest request) {

        return livraisonService.updateLivraison(id, request);
    }

    /**
     * Supprime une livraison.
     */
    @DeleteMapping("/{id}")
    public void deleteLivraison(@PathVariable Integer id) {

        livraisonService.deleteLivraison(id);
    }
}