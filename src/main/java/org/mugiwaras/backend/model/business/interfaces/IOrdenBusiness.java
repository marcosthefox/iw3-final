package org.mugiwaras.backend.model.business.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IOrdenBusiness {
    Orden load(long numeroOrden) throws BusinessException, NotFoundException;

    List<Orden> list() throws BusinessException;

    Orden add(Orden orden) throws BusinessException, FoundException, NotFoundException;

    Orden addExternal(String json) throws BusinessException, FoundException, NotFoundException;

    Orden checkIn(String json, long numeroOrden) throws NotFoundException, BusinessException;

    Orden closeOrder(long numeroOrden) throws NotFoundException, BusinessException;

    String checkOut(String json, long numeroOrden) throws NotFoundException, BusinessException, JsonProcessingException;

    String conciliacion(long numeroOrden) throws NotFoundException, JsonProcessingException, BusinessException;
}