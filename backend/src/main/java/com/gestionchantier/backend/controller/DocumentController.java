package com.gestionchantier.backend.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import com.gestionchantier.backend.dto.DocumentRequest;
import com.gestionchantier.backend.dto.DocumentResponse;
import com.gestionchantier.backend.service.DocumentService;
import com.gestionchantier.backend.service.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(
    origins = "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-5173.app.github.dev"
)
public class DocumentController {

    private final DocumentService documentService;
    private final FileStorageService fileStorageService;

    public DocumentController(
            DocumentService documentService,
            FileStorageService fileStorageService) {

        this.documentService = documentService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<DocumentResponse> getDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/chantier/{idChantier}")
public List<DocumentResponse> getDocumentsByChantier(
        @PathVariable Integer idChantier) {

    return documentService.getDocumentsByChantier(idChantier);
}

    @PostMapping(consumes = "multipart/form-data")
    public DocumentResponse saveDocument(
            @RequestParam("nom") String nom,
            @RequestParam("type") String type,
            @RequestParam("idChantier") Integer idChantier,
            @RequestParam("idUtilisateur") Integer idUtilisateur,
            @RequestParam("fichier") MultipartFile fichier) {

        String cheminFichier =
                fileStorageService.storeFile(fichier);

        DocumentRequest request = new DocumentRequest();

        request.setNom(nom);
        request.setType(type);
        request.setCheminFichier(cheminFichier);
        request.setIdChantier(idChantier);
        request.setIdUtilisateur(idUtilisateur);

        return documentService.saveDocument(request);
    }

    @PutMapping("/{id}")
    public DocumentResponse updateDocument(
            @PathVariable Integer id,
            @RequestBody DocumentRequest request) {

        return documentService.updateDocument(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(
            @PathVariable Integer id) {

        documentService.deleteDocument(id);
    }

@GetMapping("/{id}/fichier")
public ResponseEntity<Resource> getDocumentFile(
        @PathVariable Integer id) {

    Resource resource = documentService.getDocumentFile(id);

    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

    try {
        String contentType = Files.probeContentType(
                resource.getFile().toPath()
        );

        if (contentType != null) {
            mediaType = MediaType.parseMediaType(contentType);
        }

    } catch (Exception e) {
        System.out.println(
                "Impossible de déterminer le type MIME du fichier"
        );
    }

    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.inline()
                            .filename(resource.getFilename())
                            .build()
                            .toString()
            )
            .body(resource);
}
}