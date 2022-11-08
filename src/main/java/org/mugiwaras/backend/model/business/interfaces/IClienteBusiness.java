package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Cliente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface IClienteBusiness {

    public Optional<Cliente> load(Long rs) throws BusinessException, NotFoundException;

    public List<Cliente> list() throws BusinessException;

    Cliente add(Cliente cliente) throws BusinessException;
}
