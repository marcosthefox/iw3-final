package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.Producto;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface IOrdenBusiness {
    public Optional<Orden> load(long numeroOrden) throws BusinessException, NotFoundException;

    public List<Orden> list() throws BusinessException;

    void add(Orden orden) throws BusinessException;

}
