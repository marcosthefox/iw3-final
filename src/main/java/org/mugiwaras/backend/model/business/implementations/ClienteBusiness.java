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
import java.util.Optional;

@Service
@Slf4j
public class ClienteBusiness implements IClienteBusiness {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente load(String code) throws BusinessException, NotFoundException {
        Optional<Cliente> cliente;
        try {
            cliente = clienteRepository.findByCode(code);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (cliente.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro el chofer con CODIGO: " + code).build();
        }

        return cliente.get();
    }

    @Override
    public List<Cliente> list() throws BusinessException {
        try {
            return clienteRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error al traer lista de Clientes.").build();
        }
    }

    @Override
    public Cliente add(Cliente cliente) throws FoundException, BusinessException, NotFoundException {
        try {
            if (clienteRepository.existsByCode(cliente.getCode())) {
                return load(cliente.getCode());
            }
        } catch (NotFoundException e) {
        }
        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error creacion de cliente").build();
        }

    }
}

