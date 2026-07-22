package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Chantier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChantierRepository extends JpaRepository<Chantier, Integer> {
}