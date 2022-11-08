package org.mugiwaras.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.Producto;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IProductoBusiness;
import org.mugiwaras.backend.model.persistence.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoBusiness implements IProductoBusiness {

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public Producto load(long id) throws BusinessException, NotFoundException {
        Optional<Producto> producto;
        try {
            producto = productoRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if(producto.isEmpty()){
            throw NotFoundException.builder().message("No se encontro el producto con ID: " + id).build();
        }

        return producto.get();
    }

    @Override
    public List<Producto> list() throws BusinessException {
        try {
            return productoRepository.findAll();
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Producto add(Producto producto) throws BusinessException {
        try {
            load(producto.getId());
            throw FoundException.builder().message("Se encontro el producto con ID: " + producto.getId()).build();
        } catch (NotFoundException | FoundException | BusinessException e) {
        }

        try {
            return productoRepository.save(producto);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

    }
}
