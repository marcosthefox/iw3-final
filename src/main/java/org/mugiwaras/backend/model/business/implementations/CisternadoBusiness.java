package org.mugiwaras.backend.model.business.implementations;

import org.mugiwaras.backend.model.Cisternado;
import org.mugiwaras.backend.model.business.interfaces.ICisternadoBusiness;
import org.mugiwaras.backend.model.persistence.CisternadoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CisternadoBusiness implements ICisternadoBusiness {


    @Autowired
    private CisternadoRepository cisternadoRepository;

    @Override
    public Cisternado load(long id) {
        return null;
    }

    @Override
    public List<Cisternado> list() {
        return null;
    }

    @Override
    public void add(Cisternado cisternado) {

    }
}
