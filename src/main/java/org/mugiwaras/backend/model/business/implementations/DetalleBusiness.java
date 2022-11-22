package org.mugiwaras.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotAuthorizedException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.mugiwaras.backend.model.persistence.DetalleRepository;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetalleBusiness implements IDetalleBusiness {

    private final DetalleRepository detalleRepository;
    private final OrdenRepository ordenRepository;

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
    public List<Detalle> listByNumeroOrden(long numeroOrden) throws BusinessException {
        try {
            return detalleRepository.findAllById_NumeroOrden(numeroOrden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }



    @Override
    public Detalle add(Detalle detalle, long numeroOrden, int password) throws FoundException, BusinessException, NotFoundException, NotAuthorizedException {

        Optional<Orden> orden = ordenRepository.findByNumeroOrden(numeroOrden);
        if (orden.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro la orden a cargar: " + numeroOrden).build();
        }
        if (orden.get().getEstado() != 2) {
            throw BusinessException.builder().message("Error orden no disponible para la carga").build();
        }
    //    if (orden.get().getPassword() != password) {
    //        throw NotAuthorizedException.builder().message("Password Incorrecta").build();
    //    }
        if(detalle.getCaudal()<=0)
            throw BusinessException.builder().message("Valor de Caudal no valido.").build();

        if(orden.get().getUltimaMasa()>detalle.getMasa()){
            throw BusinessException.builder().message("Error valor de masa inferior al ultimo cargado").build();
        }

        try {
            if(!detalleRepository.existsDetalleByOrden_numeroOrden(numeroOrden)){
                orden.get().setFechaDetalleInicial(OffsetDateTime.now());
            }
            if(detalle.getMasa() <= orden.get().getPreset()){
                orden.get().setFechaDetalleFinal(OffsetDateTime.now());
                //Asignamos la fecha de detalle y la orden
                detalle.setFechaDetalle(OffsetDateTime.now());
                detalle.setOrden(orden.get());
                orden.get().setUltimaMasa(detalle.getMasa());
            }
            ordenRepository.save(orden.get());
            return detalleRepository.save(detalle);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error crecion de Detalle").build();
        }
    }
}
