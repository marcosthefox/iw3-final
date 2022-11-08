package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Producto;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface IProductoBusiness {

    public Producto load(long id) throws BusinessException, NotFoundException;

    public List<Producto> list() throws BusinessException;

    public Producto add(Producto producto) throws BusinessException;
}
