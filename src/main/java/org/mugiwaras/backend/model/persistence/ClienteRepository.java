package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByCode(String code);
    Boolean existsByCode(String code);
}
