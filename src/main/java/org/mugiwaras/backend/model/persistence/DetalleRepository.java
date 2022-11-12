package org.mugiwaras.backend.model.persistence;
import org.mugiwaras.backend.model.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleRepository extends JpaRepository<Detalle, Long> {
}
