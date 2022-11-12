package org.mugiwaras.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.mugiwaras.backend.model.persistence.DetalleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetalleBusiness implements IDetalleBusiness {

    private  final DetalleRepository detalleRepository;

    @Override
    public Detalle load(long rs) throws BusinessException, NotFoundException {
        Optional<Detalle> detalle;
        try {
            detalle = detalleRepository.findById(rs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if (detalle.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro el detalle: " + rs).build();
        }

        return detalle.get();

    }

    @Override
    public List<Detalle> list() throws BusinessException {
        try {
            return detalleRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Detalle add(Detalle detalle) throws FoundException, BusinessException {
      try{
            return detalleRepository.save(detalle);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error crecion de Detalle").build();
        }

    }
}
