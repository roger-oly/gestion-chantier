package com.gestionchantier.backend.service;

import com.gestionchantier.backend.entity.Document;
import com.gestionchantier.backend.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Retourne tous les documents.
     */
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    /**
     * Enregistre un nouveau document.
     */
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    /**
     * Met à jour un document.
     */
    public Document updateDocument(Integer id, Document document) {

        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));

        existingDocument.setNom(document.getNom());
        existingDocument.setType(document.getType());
        existingDocument.setCheminFichier(document.getCheminFichier());
        existingDocument.setDateUpload(document.getDateUpload());
        existingDocument.setChantier(document.getChantier());
        existingDocument.setUtilisateur(document.getUtilisateur());

        return documentRepository.save(existingDocument);
    }

    /**
     * Supprime un document.
     */
    public void deleteDocument(Integer id) {

        documentRepository.deleteById(id);
    }
}
