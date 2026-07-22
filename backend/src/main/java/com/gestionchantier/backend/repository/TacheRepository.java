package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {

}