package org.mugiwaras.backend.model.business.implementations;

import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.ICamionBusiness;

import java.util.List;

public class CamionBusiness implements ICamionBusiness {

    @Override
    public Camion load(long idCamion) throws NotFoundException, BusinessException {
        return null;
    }

    @Override
    public List<Camion> list() throws BusinessException {
        return null;
    }

    @Override
    public Camion add(Camion camion) throws FoundException, BusinessException {
        return null;
    }
}
