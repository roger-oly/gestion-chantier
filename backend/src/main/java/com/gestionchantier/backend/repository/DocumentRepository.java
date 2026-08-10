package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByChantier_IdChantier(Integer idChantier);
}