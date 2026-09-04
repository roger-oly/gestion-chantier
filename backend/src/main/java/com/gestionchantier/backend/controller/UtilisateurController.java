package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.dto.ChangePasswordRequest;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.service.UtilisateurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/{id}")
public Utilisateur getUtilisateurById(@PathVariable Integer id) {
    return utilisateurService.getUtilisateurById(id);
}

    @PostMapping
    public Utilisateur saveUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.saveUtilisateur(utilisateur);
    }

    @PutMapping("/{id}")
    public Utilisateur updateUtilisateur(
            @PathVariable Integer id,
            @RequestBody Utilisateur utilisateur) {

        return utilisateurService.updateUtilisateur(id, utilisateur);
    }

    @DeleteMapping("/{id}")
    public void deleteUtilisateur(@PathVariable Integer id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @PutMapping("/{id}/mot-de-passe")
public void changePassword(
        @PathVariable Integer id,
        @RequestBody ChangePasswordRequest request) {

    utilisateurService.changePassword(
            id,
            request.getAncienMotDePasse(),
            request.getNouveauMotDePasse()
    );
}
}