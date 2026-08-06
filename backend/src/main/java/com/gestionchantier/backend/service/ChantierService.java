package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.dto.ChantierResponse;
import com.gestionchantier.backend.dto.ChantierRequest;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChantierService {

  private final ChantierRepository chantierRepository;

private final UtilisateurRepository utilisateurRepository;


public ChantierService(
        ChantierRepository chantierRepository,
        UtilisateurRepository utilisateurRepository
        ) {

    this.chantierRepository = chantierRepository;

    this.utilisateurRepository = utilisateurRepository;

        }

    /**
     * Retourne tous les chantiers.
     */
  public List<ChantierResponse> getAllChantiers() {

    return chantierRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();

}


    /**
     * Retourne un chantier selon l'id.
     */
public ChantierResponse getChantierById(Integer id){

    Chantier chantier = chantierRepository.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Chantier introuvable")
            );


    return ChantierResponse.builder()

            .idChantier(
                chantier.getIdChantier()
            )

            .nom(
                chantier.getNom()
            )

            .description(
                chantier.getDescription()
            )

            .localisation(
                chantier.getLocalisation()
            )

            .budget(
                chantier.getBudget()
            )

            .dateDebut(
                chantier.getDateDebut()
            )

            .dateFinPrevue(
                chantier.getDateFinPrevue()
            )

            .statut(
                chantier.getStatut()
            )

            .idUtilisateur(
                chantier.getUtilisateur() != null
                ? chantier.getUtilisateur().getIdUtilisateur()
                : null
            )

            .nomUtilisateur(
                chantier.getUtilisateur() != null
                ? chantier.getUtilisateur().getNom()
                    + " "
                    + chantier.getUtilisateur().getPrenom()
                : null
            )

            .build();

}

    /**
     * Enregistre un nouveau chantier.
     */
    //public Chantier saveChantier(Chantier chantier) {
        //return chantierRepository.save(chantier);
    //}

    public ChantierResponse saveChantier(
        ChantierRequest request
    ) {

    Utilisateur utilisateur =
            utilisateurRepository.findById(
                    request.getIdUtilisateur()
            )
            .orElseThrow(
                () -> new RuntimeException("Utilisateur introuvable")
            );


    Chantier chantier = Chantier.builder()

            .nom(request.getNom())

            .description(request.getDescription())

            .localisation(request.getLocalisation())

            .budget(request.getBudget())

            .dateDebut(request.getDateDebut())

            .dateFinPrevue(request.getDateFinPrevue())

            .statut(request.getStatut())

            .utilisateur(utilisateur)

            .build();


    Chantier saved =
            chantierRepository.save(chantier);



    return ChantierResponse.builder()

            .idChantier(saved.getIdChantier())

            .nom(saved.getNom())

            .description(saved.getDescription())

            .localisation(saved.getLocalisation())

            .budget(saved.getBudget())

            .dateDebut(saved.getDateDebut())

            .dateFinPrevue(saved.getDateFinPrevue())

            .statut(saved.getStatut())

            .idUtilisateur(
                utilisateur.getIdUtilisateur()
            )

            .nomUtilisateur(
                utilisateur.getNom()
            )

            .build();

    }

    /**
     * Met à jour un chantier.
     */
    public Chantier updateChantier(Integer id, Chantier chantier) {

        Chantier existingChantier = chantierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chantier introuvable"));

        existingChantier.setNom(chantier.getNom());
        existingChantier.setDescription(chantier.getDescription());
        existingChantier.setLocalisation(chantier.getLocalisation());
        existingChantier.setBudget(chantier.getBudget());
        existingChantier.setDateDebut(chantier.getDateDebut());
        existingChantier.setDateFinPrevue(chantier.getDateFinPrevue());
        existingChantier.setStatut(chantier.getStatut());
        existingChantier.setUtilisateur(chantier.getUtilisateur());

        return chantierRepository.save(existingChantier);
    }

    /**
     * Supprime un chantier.
     */
    public void deleteChantier(Integer id) {
        chantierRepository.deleteById(id);
    }

    private ChantierResponse toResponse(Chantier chantier) {

    return ChantierResponse.builder()
            .idChantier(chantier.getIdChantier())
            .nom(chantier.getNom())
            .description(chantier.getDescription())
            .localisation(chantier.getLocalisation())
            .budget(chantier.getBudget())
            .dateDebut(chantier.getDateDebut())
            .dateFinPrevue(chantier.getDateFinPrevue())
            .statut(chantier.getStatut())
            .idUtilisateur(
                    chantier.getUtilisateur() != null
                            ? chantier.getUtilisateur().getIdUtilisateur()
                            : null
            )
            .nomUtilisateur(
                    chantier.getUtilisateur() != null
                            ? chantier.getUtilisateur().getNom() + " " + chantier.getUtilisateur().getPrenom()
                            : null
            )
            .build();
}
}