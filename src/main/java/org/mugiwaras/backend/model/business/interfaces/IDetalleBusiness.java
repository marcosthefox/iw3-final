package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Detalle;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IDetalleBusiness {
    public Detalle load(long id);

    @Nullable
    public List<Detalle> list();

    void add(Detalle detalle);
}
