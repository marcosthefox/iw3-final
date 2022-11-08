package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface ICamionBusiness {

    Optional<Camion> load(long id) throws NotFoundException, BusinessException, FoundException;

    List<Camion> list() throws BusinessException;

    Camion add(Camion camion) throws FoundException, BusinessException, NotFoundException;

}
