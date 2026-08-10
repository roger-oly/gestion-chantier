package com.gestionchantier.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadPath;

    public FileStorageService(
            @Value("${file.upload-dir}") String uploadDir
    ) {
        this.uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible de créer le dossier de stockage",
                    e
            );
        }
    }

    public String storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new RuntimeException("Nom de fichier invalide");
        }

        String extension = "";

        int lastDot = originalFilename.lastIndexOf('.');

        if (lastDot > 0) {
            extension = originalFilename.substring(lastDot);
        }

        String filename = UUID.randomUUID() + extension;

        Path targetPath = uploadPath
                .resolve(filename)
                .normalize();

        try {

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return filename;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erreur lors de l'enregistrement du fichier",
                    e
            );
        }
    }

    public Resource loadFile(String filename) {

        try {

            Path filePath = uploadPath
                    .resolve(filename)
                    .normalize();

            Resource resource =
                    new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException(
                        "Fichier introuvable : " + filename
                );
            }

            return resource;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Impossible de charger le fichier",
                    e
            );
        }
    }

    public void deleteFile(String filename) {

    if (filename == null || filename.isBlank()) {
        return;
    }

    try {

        Path filePath = uploadPath
                .resolve(filename)
                .normalize();

                System.out.println("Fichier à supprimer : " + filePath);

        // Sécurité : empêcher un chemin de sortir
        // du dossier de stockage
        if (!filePath.startsWith(uploadPath)) {
            throw new RuntimeException(
                    "Chemin de fichier invalide"
            );
        }

        Files.deleteIfExists(filePath);

    } catch (IOException e) {

        throw new RuntimeException(
                "Erreur lors de la suppression du fichier",
                e
        );
    }
}
}