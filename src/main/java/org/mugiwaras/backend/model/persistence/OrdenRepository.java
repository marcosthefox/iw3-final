package org.mugiwaras.backend.model.persistence;
import org.mugiwaras.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdenRepository  extends JpaRepository<Orden, Long> {

    Optional<Orden> findByNumeroOrden(Long numeroOrden);
}
