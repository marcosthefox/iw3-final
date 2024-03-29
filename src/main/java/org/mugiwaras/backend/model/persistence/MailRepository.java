package org.mugiwaras.backend.model.persistence;

import org.mugiwaras.backend.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    Optional<Mail> findByMail(String nombreMail);

    List<Mail> findAll();
}
