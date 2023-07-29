package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Mail;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IMailBusiness {

    Mail load(long id) throws NotFoundException, BusinessException;

    List<Mail> list() throws BusinessException;

    Mail add(Mail mail) throws FoundException, BusinessException;

    void delete(long id) throws NotFoundException, BusinessException;

    void sendSimpleMessageToAll(Float temperatura);

    void sendSimpleMessage(String to, String subject, String text);
}