package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Chofer;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface IChoferBusiness {
    Chofer load(long dniChofer) throws NotFoundException, BusinessException;

    List<Chofer> list() throws BusinessException;

    Chofer add(Chofer chofer) throws FoundException, BusinessException, NotFoundException;
}
