package com.gestionchantier.backend.controller;

import com.gestionchantier.backend.entity.Document;
import com.gestionchantier.backend.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public List<Document> getDocuments() {
        return documentService.getAllDocuments();
    }

    @PostMapping
    public Document saveDocument(@RequestBody Document document) {
        return documentService.saveDocument(document);
    }

    @PutMapping("/{id}")
    public Document updateDocument(
            @PathVariable Integer id,
            @RequestBody Document document) {

        return documentService.updateDocument(id, document);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Integer id) {
        documentService.deleteDocument(id);
    }
}
