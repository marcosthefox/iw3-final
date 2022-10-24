package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.Producto;

import java.util.List;

public interface IOrdenBusiness {
    public Orden load(long id);

    public List<Orden> list();

    void add(Orden orden);

}
