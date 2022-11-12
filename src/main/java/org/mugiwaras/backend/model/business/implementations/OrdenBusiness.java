package org.mugiwaras.backend.model.business.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.Cisternado;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.PasswordGenerator;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
import org.mugiwaras.backend.model.deserealizer.CheckInDeserealizer;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.mugiwaras.backend.util.JsonUtiles;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdenBusiness implements IOrdenBusiness {

    private final OrdenRepository ordenRepository;
    private final CamionBusiness camionBusiness;
    private final ChoferBusiness choferBusiness;
    private final CisternadoBusiness cisternadoBusiness;
    private final ClienteBusiness clienteBusiness;
    private final ProductoBusiness productoBusiness;


    @Override
    public Orden load(long numeroOrden) throws BusinessException, NotFoundException {
        Optional<Orden> r;
        try {
            r = ordenRepository.findByNumeroOrden(numeroOrden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
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
    public Orden add(Orden orden) throws BusinessException, FoundException, NotFoundException {
        try {
            load(orden.getNumeroOrden());
            throw FoundException.builder().message("Ya hay una orden con el nro =" + orden.getNumeroOrden()).build();
        } catch (NotFoundException e) {
        }
        Camion camion;
        try {
            if (!camionBusiness.exists(orden.getCamion().getPatente())) {
                // cuando el camion es nuevo
                camionBusiness.add(orden.getCamion());
                for (Cisternado cisternado : orden.getCamion().getDatosCisterna()) {
                    cisternado.setCamion(orden.getCamion());
                    cisternadoBusiness.add(cisternado);
                }
            } else {
                // camion q ya existe
                camion = camionBusiness.add(orden.getCamion());
                orden.setCamion(camion);
            }

            choferBusiness.add(orden.getChofer());
            clienteBusiness.add(orden.getCliente());
            productoBusiness.add(orden.getProducto());
            orden.setEstado(1);

            return ordenRepository.save(orden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error cracion de la orden").build();
        }

    }

    @Override
    public Orden checkIn(String json, long numeroOrden) throws NotFoundException, BusinessException {
        ObjectMapper mapper = JsonUtiles.getObjectMapper(Orden.class, new CheckInDeserealizer(Orden.class));
        Orden ordenNew;
        try {
            ordenNew = mapper.readValue(json, Orden.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        Orden orden = null;
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
        }

        orden.setTara(ordenNew.getTara());
        orden.setFechaPesajeInicial(OffsetDateTime.now());
        orden.setPassword(PasswordGenerator.generateFiveDigitPassword());
        orden.setEstado(2);
        return ordenRepository.save(orden);
    }

    @Override
    public Orden closeOrder(long numeroOrden) throws NotFoundException, BusinessException {
        Orden orden = new Orden();
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
        }
        orden.setEstado(3);
        // asigna alguna fecha a la orden??
        return ordenRepository.save(orden);
    }

}
