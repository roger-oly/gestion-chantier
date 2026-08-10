package com.gestionchantier.backend.service;

import com.gestionchantier.backend.dto.DocumentRequest;
import com.gestionchantier.backend.dto.DocumentResponse;
import com.gestionchantier.backend.entity.Chantier;
import com.gestionchantier.backend.entity.Document;
import com.gestionchantier.backend.entity.Utilisateur;
import com.gestionchantier.backend.exception.ResourceNotFoundException;
import com.gestionchantier.backend.repository.ChantierRepository;
import com.gestionchantier.backend.repository.DocumentRepository;
import com.gestionchantier.backend.repository.UtilisateurRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ChantierRepository chantierRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FileStorageService fileStorageService;

    public DocumentService(
            DocumentRepository documentRepository,
            ChantierRepository chantierRepository,
            UtilisateurRepository utilisateurRepository,
            FileStorageService fileStorageService) {

        this.documentRepository = documentRepository;
        this.chantierRepository = chantierRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Retourne tous les documents.
     */
    public List<DocumentResponse> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
 * Retourne les documents d'un chantier.
 */
public List<DocumentResponse> getDocumentsByChantier(Integer idChantier) {

    // Vérifie d'abord que le chantier existe
    chantierRepository.findById(idChantier)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Chantier introuvable"
                    ));

    return documentRepository
            .findByChantier_IdChantier(idChantier)
            .stream()
            .map(this::toResponse)
            .toList();
}

    /**
     * Retourne le fichier physique associé à un document.
     */
    public Resource getDocumentFile(Integer id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Document introuvable"
                        ));

        return fileStorageService.loadFile(
                document.getCheminFichier()
        );
    }

    /**
     * Enregistre un nouveau document.
     */
    public DocumentResponse saveDocument(DocumentRequest request) {

        Chantier chantier = chantierRepository
                .findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"
                        ));

        Utilisateur utilisateur = utilisateurRepository
                .findById(request.getIdUtilisateur())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        ));

        Document document = Document.builder()
                .nom(request.getNom())
                .type(request.getType())
                .cheminFichier(request.getCheminFichier())
                .dateUpload(LocalDateTime.now())
                .chantier(chantier)
                .utilisateur(utilisateur)
                .build();

        return toResponse(
                documentRepository.save(document)
        );
    }

    /**
     * Met à jour un document.
     */
    public DocumentResponse updateDocument(
            Integer id,
            DocumentRequest request) {

        Document existingDocument = documentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Document introuvable"
                        ));

        Chantier chantier = chantierRepository
                .findById(request.getIdChantier())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Chantier introuvable"
                        ));

        Utilisateur utilisateur = utilisateurRepository
                .findById(request.getIdUtilisateur())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Utilisateur introuvable"
                        ));

        existingDocument.setNom(request.getNom());
        existingDocument.setType(request.getType());
        existingDocument.setCheminFichier(
                request.getCheminFichier()
        );
        existingDocument.setChantier(chantier);
        existingDocument.setUtilisateur(utilisateur);

        return toResponse(
                documentRepository.save(existingDocument)
        );
    }

    /**
     * Supprime un document.
     */
   public void deleteDocument(Integer id) {

    Document document = documentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Document introuvable"
                    ));

    // Suppression du fichier physique
    fileStorageService.deleteFile(
            document.getCheminFichier()
    );

    // Suppression de l'enregistrement en base
    documentRepository.delete(document);
}

    /**
     * Transforme une Entity Document en DocumentResponse.
     */
    private DocumentResponse toResponse(Document document) {

        return DocumentResponse.builder()
                .idDocument(document.getIdDocument())
                .nom(document.getNom())
                .type(document.getType())
                .cheminFichier(document.getCheminFichier())
                .dateUpload(document.getDateUpload())
                .idChantier(
                        document.getChantier().getIdChantier()
                )
                .nomChantier(
                        document.getChantier().getNom()
                )
                .idUtilisateur(
                        document.getUtilisateur().getIdUtilisateur()
                )
                .nomUtilisateur(
                        document.getUtilisateur().getNom()
                                + " "
                                + document.getUtilisateur().getPrenom()
                )
                .build();
    }
}
