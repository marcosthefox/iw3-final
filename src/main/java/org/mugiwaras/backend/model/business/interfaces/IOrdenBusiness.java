package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.Producto;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IOrdenBusiness {
    public Orden load(long numeroOrden) throws BusinessException, NotFoundException;

    public List<Orden> list() throws BusinessException;

    void add(Orden orden) throws BusinessException;

}
