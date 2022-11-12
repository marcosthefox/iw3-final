package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IDetalleBusiness {
    public Detalle load(long id) throws BusinessException, NotFoundException;

    @Nullable
    public List<Detalle> list() throws BusinessException;

    Detalle add(Detalle detalle,long numeroOrden,int password) throws FoundException, BusinessException;
}
