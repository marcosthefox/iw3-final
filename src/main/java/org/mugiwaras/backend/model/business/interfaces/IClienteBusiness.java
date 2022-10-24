package org.mugiwaras.backend.model.business.interfaces;

import org.mugiwaras.backend.model.Cliente;

import java.util.List;

public interface IClienteBusiness {

    public Cliente load(long id);

    public List<Cliente> list();

    void add(Cliente cliente);
}
