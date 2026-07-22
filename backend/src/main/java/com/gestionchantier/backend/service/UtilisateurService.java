package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Retourne tous les utilisateurs.
     */
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    /**
     * Enregistre un nouvel utilisateur.
     */
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Met à jour un utilisateur.
     */
    public Utilisateur updateUtilisateur(Integer id, Utilisateur utilisateur) {

        Utilisateur existingUtilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        existingUtilisateur.setNom(utilisateur.getNom());
        existingUtilisateur.setPrenom(utilisateur.getPrenom());
        existingUtilisateur.setEmail(utilisateur.getEmail());
        existingUtilisateur.setMotDePasse(utilisateur.getMotDePasse());
        existingUtilisateur.setTelephone(utilisateur.getTelephone());
        existingUtilisateur.setStatut(utilisateur.getStatut());
        existingUtilisateur.setRole(utilisateur.getRole());

        return utilisateurRepository.save(existingUtilisateur);
    }

    /**
     * Supprime un utilisateur.
     */
    public void deleteUtilisateur(Integer id) {

        utilisateurRepository.deleteById(id);
    }
}