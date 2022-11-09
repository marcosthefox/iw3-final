package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Cliente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface IClienteBusiness {

    Cliente load(long rs) throws NotFoundException, BusinessException, FoundException;

    List<Cliente> list() throws BusinessException;

    Cliente add(Cliente cliente) throws NotFoundException, BusinessException, FoundException;
}
