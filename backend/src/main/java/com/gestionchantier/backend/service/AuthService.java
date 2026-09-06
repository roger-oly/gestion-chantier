package com.gestionchantier.backend.service;

import com.gestionchantier.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import com.gestionchantier.backend.dto.LoginResponse;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UtilisateurRepository utilisateurRepository;


public AuthService(
        UtilisateurRepository utilisateurRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService) {

    this.utilisateurRepository = utilisateurRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}


    public LoginResponse login(String email, String motDePasse){

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );

String motDePasseStocke = utilisateur.getMotDePasse();

if (passwordEncoder.matches(
        motDePasse,
        motDePasseStocke)) {

    String token = jwtService.generateToken(
        utilisateur.getIdUtilisateur(),
        utilisateur.getEmail(),
        utilisateur.getRole().getLibelle()
);

return new LoginResponse(
        utilisateur.getIdUtilisateur(),
        utilisateur.getNom(),
        utilisateur.getPrenom(),
        utilisateur.getEmail(),
        utilisateur.getTelephone(),
        utilisateur.getStatut(),
        utilisateur.getRole(),
        token
);
}

/*
 * Migration progressive des anciens mots de passe
 * stockés en clair vers BCrypt.
 */
if (motDePasseStocke.equals(motDePasse)) {

    utilisateur.setMotDePasse(
            passwordEncoder.encode(motDePasse)
    );

    utilisateurRepository.save(utilisateur);

    String token = jwtService.generateToken(
        utilisateur.getIdUtilisateur(),
        utilisateur.getEmail(),
        utilisateur.getRole().getLibelle()
);

System.out.println("JWT généré : " + token);

return new LoginResponse(
        utilisateur.getIdUtilisateur(),
        utilisateur.getNom(),
        utilisateur.getPrenom(),
        utilisateur.getEmail(),
        utilisateur.getTelephone(),
        utilisateur.getStatut(),
        utilisateur.getRole(),
        token
);
}

throw new RuntimeException("Mot de passe incorrect");
    }
}