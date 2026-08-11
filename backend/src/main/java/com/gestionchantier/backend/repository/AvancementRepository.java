package com.gestionchantier.backend.repository;

import com.gestionchantier.backend.entity.Avancement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvancementRepository extends JpaRepository<Avancement, Integer> {

    List<Avancement> findByChantier_IdChantierOrderByDateMiseAJourDesc(
            Integer idChantier
    );
}