package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Producto;

import java.util.List;

public interface IProductoBusiness {

    public Producto load(long id);

    public List<Producto> list();

    void add(Producto producto);
}
