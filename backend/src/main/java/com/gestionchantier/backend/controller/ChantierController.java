package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.service.ChantierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chantiers")
public class ChantierController {

    private final ChantierService chantierService;

    public ChantierController(ChantierService chantierService) {
        this.chantierService = chantierService;
    }

    @GetMapping
    public List<Chantier> getChantiers() {
        return chantierService.getAllChantiers();
    }

    @PostMapping
    public Chantier saveChantier(@RequestBody Chantier chantier) {
        return chantierService.saveChantier(chantier);
    }

    @PutMapping("/{id}")
    public Chantier updateChantier(
            @PathVariable Integer id,
            @RequestBody Chantier chantier) {

        return chantierService.updateChantier(id, chantier);
    }

    @DeleteMapping("/{id}")
    public void deleteChantier(@PathVariable Integer id) {
        chantierService.deleteChantier(id);
    }
}
