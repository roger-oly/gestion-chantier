package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {

    List<Tache> findByChantier_IdChantier(Integer idChantier);

}