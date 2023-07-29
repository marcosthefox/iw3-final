package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Producto;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IProductoBusiness {

    Producto load(String code) throws BusinessException, NotFoundException;

    List<Producto> list() throws BusinessException;

    Producto add(Producto producto) throws BusinessException, FoundException, NotFoundException;
}
