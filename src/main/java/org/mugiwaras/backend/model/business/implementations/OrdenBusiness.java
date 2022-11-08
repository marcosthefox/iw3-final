package org.mugiwaras.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrdenBusiness implements IOrdenBusiness {

    @Autowired
    private OrdenRepository ordenRepository;

    @Override
    public Orden load(long numeroOrden) throws BusinessException, NotFoundException  {
        Optional<Orden> r;
        try {
            r= ordenRepository.findByNumeroOrden(numeroOrden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if(r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra la orden con id=" + numeroOrden).build();
        }
        return r.get();
    }

    @Override
    public List<Orden> list() throws BusinessException {
        try {
            return ordenRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void add(Orden orden) throws BusinessException{
        try {
            load(orden.getNumeroOrden());
           
            throw NotFoundException.builder().message("Ya hay una orden con el nro =" + orden.getNumeroOrden()).build();
        } catch (NotFoundException e) {
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        try {
            ordenRepository.save(orden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

    }
}
