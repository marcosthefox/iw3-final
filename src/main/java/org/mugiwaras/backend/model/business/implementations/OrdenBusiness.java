package org.mugiwaras.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Chofer;
import org.mugiwaras.backend.model.Cisternado;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
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

    //Los utilizo para armar todo
    @Autowired
    private CamionBusiness camionBusiness;
    @Autowired
    private ChoferBusiness choferBusiness;
    @Autowired
    private CisternadoBusiness cisternadoBusiness;
    @Autowired
    private ClienteBusiness clienteBusiness;
    @Autowired
    private ProductoBusiness productoBusiness;



    @Override
    public Optional<Orden> load(long numeroOrden) throws BusinessException, NotFoundException  {
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
        return r;
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
            throw FoundException.builder().message("Ya hay una orden con el nro =" + orden.getNumeroOrden()).build();
        } catch (FoundException | NotFoundException e) {
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        //camion
        try {
            camionBusiness.add(orden.getCamion());
            productoBusiness.add(orden.getProducto());
            clienteBusiness.add(orden.getCliente());
            /*for(Cisternado cisternado : orden.getCamion().getDatosCisterna()){
                cisternadoBusiness.add(cisternado);
            }*/
            choferBusiness.add(orden.getChofer());

        } catch (FoundException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e) {
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
