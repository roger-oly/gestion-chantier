package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;


    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    public Utilisateur login(String email, String motDePasse) {

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );


        if (!utilisateur.getMotDePasse().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }


        return utilisateur;
    }
}