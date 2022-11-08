package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {

}
