package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Avancement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvancementRepository extends JpaRepository<Avancement, Integer> {

}