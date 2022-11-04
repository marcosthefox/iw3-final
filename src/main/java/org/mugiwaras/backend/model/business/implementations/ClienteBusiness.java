package org.mugiwaras.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Cliente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IClienteBusiness;
import org.mugiwaras.backend.model.persistence.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClienteBusiness implements IClienteBusiness {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente load(long id) throws BusinessException, NotFoundException {
        Cliente cliente;
        try {
            cliente = clienteRepository.findClienteByRazonSocial(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if (cliente.equals(null)) {
            throw NotFoundException.builder().message("No se encontro el chofer con razon social: " + id).build();
        }

        return cliente;

    }

    @Override
    public List<Cliente> list() throws BusinessException {
        try {
            return clienteRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Cliente add(Cliente cliente) throws BusinessException {
        try {
            load(cliente.getRazonSocial());
            throw FoundException.builder().message("Se encontro el chofer con DNI: " + cliente.getRazonSocial()).build();
        } catch (NotFoundException | FoundException | BusinessException e) {
        }

        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

    }
}

