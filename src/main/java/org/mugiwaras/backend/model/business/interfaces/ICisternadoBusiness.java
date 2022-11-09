package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Cisternado;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;

import java.util.List;

public interface ICisternadoBusiness {


    List<Cisternado> list(long idCamion);

    void add(Cisternado cisternado) throws BusinessException;

}
