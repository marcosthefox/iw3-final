package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.DetalleReciente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotAuthorizedException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface IDetalleBusiness {
    Detalle load(long id) throws BusinessException, NotFoundException;

    @Nullable
    List<Detalle> list() throws BusinessException;

    void add(Detalle detalle, long numeroOrden) throws BusinessException;

    List<Detalle> listByNumeroOrden(long numeroOrden) throws BusinessException;

    void procesarDetalle(Detalle detalle, long numeroOrden, int password) throws NotFoundException, BusinessException, NotAuthorizedException;

    Map<Long, DetalleReciente> getOrdenes();

    void borrarMapaOrdenes();
}
