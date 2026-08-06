package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.dto.ChantierResponse;
import com.gestionchantier.backend.dto.ChantierRequest;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.service.ChantierService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chantiers")
@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"
)
public class ChantierController {

    private final ChantierService chantierService;

    public ChantierController(ChantierService chantierService) {
        this.chantierService = chantierService;
    }

    @GetMapping
    public List<ChantierResponse> getAllChantiers() {
        return chantierService.getAllChantiers();
    }

      @GetMapping("/{id}")
    public ChantierResponse getChantierById(
            @PathVariable Integer id
    ){

        return chantierService.getChantierById(id);

    }

   @PostMapping
    public ChantierResponse saveChantier(
        @RequestBody ChantierRequest request
    ) {

    return chantierService.saveChantier(request);

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
