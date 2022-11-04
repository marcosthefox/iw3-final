package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.Cisternado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CisternadoRepository extends JpaRepository<Cisternado, Camion> {

    Cisternado findOneById(long id);

    List<Cisternado> findAllById_IdCamion(long idCamion);
}
