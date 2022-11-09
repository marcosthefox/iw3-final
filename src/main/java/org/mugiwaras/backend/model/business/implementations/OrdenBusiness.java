package org.mugiwaras.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Cisternado;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.springframework.stereotype.Service;

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
    public void add(Orden orden) throws BusinessException, FoundException, NotFoundException {
        try {
            load(orden.getNumeroOrden());
            throw FoundException.builder().message("Ya hay una orden con el nro =" + orden.getNumeroOrden()).build();
        } catch (NotFoundException e) {
        }
        try {

            if (!camionBusiness.exists(orden.getCamion().getPatente())) {
                // esto se ejecuta cuando el camion es nuevo
                camionBusiness.add(orden.getCamion());
                for (Cisternado cisternado : orden.getCamion().getDatosCisterna()) {
                    cisternado.setCamion(orden.getCamion());
                    cisternadoBusiness.add(cisternado);
                }
            } else {
                // camion q ya existe con cisterna q tambien existe
                camionBusiness.add(orden.getCamion());
                List<Cisternado> cisternadoList = cisternadoBusiness.list(orden.getCamion().getId_camion());
                for (Cisternado cisternado : cisternadoList) {
                    cisternadoBusiness.add(cisternado);
                }
            }

            choferBusiness.add(orden.getChofer());
            clienteBusiness.add(orden.getCliente());
            productoBusiness.add(orden.getProducto());

            ordenRepository.save(orden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error cracion de la orden").build();
        }

    }
}
