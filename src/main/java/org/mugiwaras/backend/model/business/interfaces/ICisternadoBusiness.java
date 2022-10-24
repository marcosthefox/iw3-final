package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Cisternado;

import java.util.List;

public interface ICisternadoBusiness {

    public Cisternado load(long id);

    public List<Cisternado> list();

    void add(Cisternado cisternado);

}
