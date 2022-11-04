package org.mugiwaras.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Chofer;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IChoferBusiness;
import org.mugiwaras.backend.model.persistence.ChoferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ChoferBusiness implements IChoferBusiness {

    @Autowired
    private ChoferRepository choferRepository;


    @Override
    public Chofer load(long dniChofer) throws NotFoundException, BusinessException {
        Chofer chofer;
        try {
            chofer = choferRepository.findOneByDni(dniChofer);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if(chofer.equals(null)){
            throw NotFoundException.builder().message("No se encontro el chofer con DNI: " + dniChofer ).build();
        }

        return chofer;
    }

    @Override
    public List<Chofer> list() throws BusinessException {
        try {
            return choferRepository.findAll();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

    }

    @Override
    public Chofer add(Chofer chofer) throws FoundException, BusinessException {
        try {
            load(chofer.getDni());
            throw FoundException.builder().message("Se encontro el chofer con DNI: " + chofer.getDni()).build();
        } catch (NotFoundException e) {}

        try {
            return choferRepository.save(chofer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

    }
}
//El update no iria, pq no podriamos modificar ninguno.