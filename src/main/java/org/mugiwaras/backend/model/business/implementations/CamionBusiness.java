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

    @Autowired
    private CamionRepository camionRepository;

    @Override
    public Camion load(String patente) throws NotFoundException, BusinessException {
        Optional<Camion> camion;
        try {
            camion = camionRepository.findByPatente(patente);
        } catch (Exception e) {
            throw BusinessException.builder().ex(e).build();
        }
        if (camion.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el camion " + patente).build();
        }
        return camion.get();
    }

    @Override
    public Boolean exists(String patente) {
        return camionRepository.existsByPatente(patente);
    }

    @Override
    public List<Camion> list() throws BusinessException {
        return null;
    }

    @Override
    public Camion add(Camion camion) throws FoundException, BusinessException, NotFoundException {
        try {
            if (camionRepository.existsByPatente(camion.getPatente())) {
                return load(camion.getPatente());
            }
        } catch (NotFoundException e) {
        }
        try {
            return camionRepository.save(camion);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error creacion de camion").build();
        }
    }
}
