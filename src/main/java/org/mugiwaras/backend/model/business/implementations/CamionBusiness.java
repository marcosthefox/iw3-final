package org.mugiwaras.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.ICamionBusiness;
import org.mugiwaras.backend.model.persistence.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CamionBusiness implements ICamionBusiness {

    @Autowired(required = false)
    private CamionRepository camionRepository;

    @Override
    public Optional<Camion> load(long id) throws NotFoundException, BusinessException {
        Optional<Camion> camion;
        try {
            camion = camionRepository.findById(id);
        } catch (Exception e) {
                throw BusinessException.builder().ex(e).build();
        }
        if (camion.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra " + id).build();
        }
            return camion;
        }

    @Override
    public List<Camion> list() throws BusinessException {
        return null;
    }

    @Override
    public Camion add(Camion camion) throws FoundException, BusinessException, NotFoundException {
        try{
            load(camion.getId_camion());
            throw FoundException.builder().message("se encontro el camion con ID: " + camion.getId_camion()).build();
        }catch (NotFoundException e){
        }

        try {
            return camionRepository.save(camion);
        } catch (Exception e){
            throw BusinessException.builder().ex(e).build();
        }
    }
}
