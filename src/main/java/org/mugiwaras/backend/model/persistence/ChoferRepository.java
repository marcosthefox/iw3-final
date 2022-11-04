package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long> {

    Chofer findOneByDni(long dni);

}
