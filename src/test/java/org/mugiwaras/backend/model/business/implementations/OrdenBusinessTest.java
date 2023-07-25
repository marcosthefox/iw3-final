package org.mugiwaras.backend.model.business.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrdenBusinessTest {

    @InjectMocks
    private OrdenBusiness ordenBusiness;

    @Mock
    private OrdenRepository ordenRepository;

//    @Test
//    public void checkout_OK() throws BusinessException, NotFoundException, JsonProcessingException {
//        //given
//        Orden orden = Orden.builder().numeroOrden(1L).build();
//        given(ordenBusiness.load(any(Long.class))).willReturn(orden);
//        given(ordenRepository.save(any(Orden.class))).willReturn(Orden.builder().numeroOrden(1L).build());
//
//        // when
//        String result = ordenBusiness.checkOut("test123", 1L);
//
//        // then
//        then(result).isNotNull();
//    }

//    @Test
//    public void conciliacion_OK() throws BusinessException, NotFoundException, JsonProcessingException {
//        //given
//        Orden orden = Orden.builder().numeroOrden(1).CodigoExterno("test123").build();
//        given(ordenBusiness.load(any(long.class))).willReturn(orden);
//
//        //when
//        String result = ordenBusiness.conciliacion(1);
//        //then
//        then(result).isNotNull();
//    }

    @Test
    public void load_OK() throws BusinessException, NotFoundException {
        //given
        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().numeroOrden(1).build()));

        //when
        Orden result = ordenBusiness.load(1L);
        //then
        then(result).isNotNull();
    }

    @Test
    public void load_BusinessException() {
        //given
        given(ordenRepository.findByNumeroOrden(any(Long.class))).willThrow(DuplicateKeyException.class);

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.load(1L));
        //Then
        then(thrown.getMessage());
    }

    @Test
    public void list_OK() throws BusinessException {
        //given
        given(ordenRepository.findAll()).willReturn(List.of());

        //when
        List<Orden> result = ordenBusiness.list();
        //then
        then(result).isNotNull();
    }

    @Test
    public void list_Error() {
        // given
        given(ordenRepository.findAll()).willThrow(DuplicateKeyException.class);

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.list());
        //Then
        then(thrown.getMessage()).isEqualTo("Error al traer las ordenes.");
    }

}
