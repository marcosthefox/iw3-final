package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {

    Optional<Camion> findByPatente(String patente);

    Boolean existsByPatente(String patente);
}