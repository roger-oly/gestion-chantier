package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    List<Incident> findByChantier_IdChantier(Integer idChantier);

}