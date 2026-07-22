package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Avancement;
import com.gestionchantier.backend.service.AvancementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avancements")
public class AvancementController {

    private final AvancementService avancementService;

    public AvancementController(AvancementService avancementService) {
        this.avancementService = avancementService;
    }

    @GetMapping
    public List<Avancement> getAvancements() {
        return avancementService.getAllAvancements();
    }

    @PostMapping
    public Avancement saveAvancement(@RequestBody Avancement avancement) {
        return avancementService.saveAvancement(avancement);
    }

    @PutMapping("/{id}")
    public Avancement updateAvancement(
            @PathVariable Integer id,
            @RequestBody Avancement avancement) {

        return avancementService.updateAvancement(id, avancement);
    }

    @DeleteMapping("/{id}")
    public void deleteAvancement(@PathVariable Integer id) {
        avancementService.deleteAvancement(id);
    }
}
