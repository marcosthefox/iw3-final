package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findClienteByRazonSocial(long razonSocial);


}
