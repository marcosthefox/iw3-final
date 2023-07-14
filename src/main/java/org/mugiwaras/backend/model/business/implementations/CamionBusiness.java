package org.mugiwaras.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.Cisternado;
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
@RequiredArgsConstructor
public class CamionBusiness implements ICamionBusiness {


    private final CamionRepository camionRepository;

    private final CisternadoBusiness cisternadoBusiness;

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
            Camion result = camionRepository.save(camion);
            for (Cisternado cisternado : camion.getDatosCisterna()){
                cisternado.setCamion(result);
                cisternadoBusiness.add(cisternado);
            }
            return result;
        } catch (Exception e) {
            throw BusinessException.builder().message("Error creacion de camion").build();
        }
    }
}