package com.gestionchantier.backend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final NotificationService notificationService;
    private final DocumentRepository documentRepository;
    private final ChantierRepository chantierRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FileStorageService fileStorageService;

    public DocumentService(
        DocumentRepository documentRepository,
        ChantierRepository chantierRepository,
        UtilisateurRepository utilisateurRepository,
        FileStorageService fileStorageService,
        NotificationService notificationService) {

    this.documentRepository = documentRepository;
    this.chantierRepository = chantierRepository;
    this.utilisateurRepository = utilisateurRepository;
    this.fileStorageService = fileStorageService;
    this.notificationService = notificationService;
}

    /**
     * Retourne tous les documents.
     */
public List<DocumentResponse> getAllDocuments() {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    String email = authentication.getName();

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                            || authority.getAuthority().equals("ROLE_DIRECTION")
                    );

    if (administrateurOuDirection) {
        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    return documentRepository.findAll()
            .stream()
            .filter(document ->
                    document.getChantier() != null
                    && document.getChantier().getUtilisateur() != null
                    && email.equals(
                            document.getChantier()
                                    .getUtilisateur()
                                    .getEmail()
                    )
            )
            .map(this::toResponse)
            .toList();
}

    /**
 * Retourne les documents d'un chantier.
 */
public List<DocumentResponse> getDocumentsByChantier(Integer idChantier) {

    Chantier chantier = chantierRepository
            .findById(idChantier)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Chantier introuvable"
                    ));

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                            || authority.getAuthority().equals("ROLE_DIRECTION")
                    );

    if (!administrateurOuDirection) {

        String email = authentication.getName();

        if (chantier.getUtilisateur() == null
                || !email.equals(
                        chantier.getUtilisateur().getEmail()
                )) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à consulter les documents de ce chantier"
            );
        }
    }

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

    Document document = documentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Document introuvable"
                    ));

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                            || authority.getAuthority().equals("ROLE_DIRECTION")
                    );

    if (!administrateurOuDirection) {

        String email = authentication.getName();

        if (document.getChantier() == null
                || document.getChantier().getUtilisateur() == null
                || !email.equals(
                        document.getChantier()
                                .getUtilisateur()
                                .getEmail()
                )) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à consulter ce document"
            );
        }
    }

    return fileStorageService.loadFile(
            document.getCheminFichier()
    );
}

    /**
     * Enregistre un nouveau document.
     */
public DocumentResponse saveDocument(DocumentRequest request) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                            || authority.getAuthority().equals("ROLE_DIRECTION")
                    );

    boolean responsableProjetOuChefChantier =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_RESPONSABLE_PROJET")
                            || authority.getAuthority().equals("ROLE_CHEF_CHANTIER")
                    );

    if (!administrateurOuDirection
            && !responsableProjetOuChefChantier) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à ajouter un document"
        );
    }

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

    /*
     * L'utilisateur connecté ne peut pas
     * créer un document au nom d'un autre utilisateur.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez pas ajouter un document au nom d'un autre utilisateur"
        );
    }

    /*
     * Les utilisateurs qui ne sont ni administrateur
     * ni direction doivent appartenir au chantier.
     */
    if (!administrateurOuDirection) {

        if (chantier.getUtilisateur() == null
                || !authentication.getName().equals(
                        chantier.getUtilisateur().getEmail()
                )) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à ajouter un document sur ce chantier"
            );
        }
    }

    Document document = Document.builder()
            .nom(request.getNom())
            .type(request.getType())
            .cheminFichier(request.getCheminFichier())
            .dateUpload(LocalDateTime.now())
            .chantier(chantier)
            .utilisateur(utilisateur)
            .build();

    Document savedDocument =
            documentRepository.save(document);

    /*
     * Notification du responsable du chantier.
     */
    if (chantier.getUtilisateur() != null
            && chantier.getUtilisateur().getIdUtilisateur() != null) {

        Integer idDestinataire =
                chantier.getUtilisateur().getIdUtilisateur();

        notificationService.createNotification(
                idDestinataire,
                "Nouveau document",
                "Un nouveau document a été ajouté au chantier "
                        + chantier.getNom()
                        + " : "
                        + document.getNom(),
                "DOCUMENT"
        );
    }

    return toResponse(savedDocument);
}

        

    /**
     * Met à jour un document.
     */
 public DocumentResponse updateDocument(
        Integer id,
        DocumentRequest request) {

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    boolean administrateurOuDirection =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMINISTRATEUR")
                            || authority.getAuthority().equals("ROLE_DIRECTION")
                    );

    boolean responsableProjetOuChefChantier =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_RESPONSABLE_PROJET")
                            || authority.getAuthority().equals("ROLE_CHEF_CHANTIER")
                    );

    if (!administrateurOuDirection
            && !responsableProjetOuChefChantier) {

        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à modifier un document"
        );
    }

    Document existingDocument = documentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Document introuvable"
                    ));

    /*
     * Vérification du chantier actuellement associé
     * au document.
     */
    if (!administrateurOuDirection) {

        if (existingDocument.getChantier() == null
                || existingDocument.getChantier().getUtilisateur() == null
                || !authentication.getName().equals(
                        existingDocument.getChantier()
                                .getUtilisateur()
                                .getEmail()
                )) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à modifier ce document"
            );
        }
    }

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

    /*
     * Empêche de modifier le document
     * au nom d'un autre utilisateur.
     */
    if (!utilisateur.getEmail()
            .equals(authentication.getName())) {

        throw new AccessDeniedException(
                "Vous ne pouvez pas modifier un document au nom d'un autre utilisateur"
        );
    }

    /*
     * Vérification du nouveau chantier.
     */
    if (!administrateurOuDirection) {

        if (chantier.getUtilisateur() == null
                || !authentication.getName().equals(
                        chantier.getUtilisateur().getEmail()
                )) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à déplacer ce document vers ce chantier"
            );
        }
    }

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

    Authentication authentication =
            SecurityContextHolder
                    .getContext()
                    .getAuthentication();

    if (authentication == null) {
        throw new AccessDeniedException(
                "Utilisateur non authentifié"
        );
    }

    Document document = documentRepository
            .findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Document introuvable"
                    ));

    boolean administrateur =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMINISTRATEUR")
                    );

    boolean direction =
            authentication.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_DIRECTION")
                    );

    if (!administrateur && !direction) {
        throw new AccessDeniedException(
                "Vous n'êtes pas autorisé à supprimer un document"
        );
    }

    if (direction) {

        String emailUtilisateur =
                authentication.getName();

        if (document.getChantier() == null
                || document.getChantier().getUtilisateur() == null
                || !document.getChantier()
                        .getUtilisateur()
                        .getEmail()
                        .equals(emailUtilisateur)) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à supprimer ce document"
            );
        }
    }

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
