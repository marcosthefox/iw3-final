package org.mugiwaras.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.mugiwaras.backend.model.persistence.DetalleRepository;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetalleBusiness implements IDetalleBusiness {

    private  final DetalleRepository detalleRepository;
    private  final OrdenRepository ordenRepository;

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
    public Detalle add(Detalle detalle,long numeroOrden,int password) throws FoundException, BusinessException {
      try{
          Optional<Orden> orden=ordenRepository.findByNumeroOrden(numeroOrden);
          if(orden.isEmpty()){
              throw NotFoundException.builder().message("No se encontro la orden a cargar: " + numeroOrden).build();
          }
          if(orden.get().getEstado()!=2){
              throw BusinessException.builder().message("Error orden no disponible para la carga").build();
          }
          if(orden.get().getPassword() != password){
              throw BusinessException.builder().message("Password Incorrecta").build();
          }
          //Asignamos la fecha de detalle y la orden
          detalle.setFechaDetalle(OffsetDateTime.now());
          detalle.setOrden(orden.get());
            return detalleRepository.save(detalle);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error crecion de Detalle").build();
        }

    }
}
