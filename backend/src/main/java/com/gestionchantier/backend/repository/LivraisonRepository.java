package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Integer> {

}