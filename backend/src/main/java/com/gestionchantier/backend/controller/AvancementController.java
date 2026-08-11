package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Avancement;
import com.gestionchantier.backend.service.AvancementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"
)
@RestController
@RequestMapping("/api/avancements")
public class AvancementController {

    private final AvancementService avancementService;

    public AvancementController(
            AvancementService avancementService) {

        this.avancementService = avancementService;
    }

    /**
     * Retourne tous les avancements.
     */
    @GetMapping
    public List<Avancement> getAvancements() {
        return avancementService.getAllAvancements();
    }

    /**
     * Retourne les avancements d'un chantier.
     */
    @GetMapping("/chantier/{idChantier}")
    public List<Avancement> getAvancementsByChantier(
            @PathVariable Integer idChantier) {

        return avancementService
                .getAvancementsByChantier(idChantier);
    }

    /**
     * Retourne le pourcentage actuel calculé
     * à partir des tâches terminées.
     */
    @GetMapping("/chantier/{idChantier}/pourcentage")
    public int getPourcentage(
            @PathVariable Integer idChantier) {

        return avancementService
                .calculerPourcentage(idChantier);
    }

    /**
     * Enregistre une validation d'avancement.
     *
     * Le pourcentage et la date sont calculés
     * automatiquement par le backend.
     */
    @PostMapping
    public Avancement saveAvancement(
            @RequestParam Integer idChantier,
            @RequestParam Integer idUtilisateur,
            @RequestParam(required = false) String commentaire) {

        return avancementService.saveAvancement(
                idChantier,
                idUtilisateur,
                commentaire
        );
    }

    /**
     * Supprime un enregistrement d'avancement.
     */
    @DeleteMapping("/{id}")
    public void deleteAvancement(
            @PathVariable Integer id) {

        avancementService.deleteAvancement(id);
    }
}