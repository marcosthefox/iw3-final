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
    public Optional<Cliente> load(Long rs) throws BusinessException, NotFoundException {
        Optional<Cliente> cliente;
        try {
            cliente = clienteRepository.findById(rs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if (cliente.equals(null)) {
            throw NotFoundException.builder().message("No se encontro el chofer con razon social: " + rs).build();
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

