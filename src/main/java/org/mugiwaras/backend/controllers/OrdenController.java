package org.mugiwaras.backend.controllers;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotAuthorizedException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
import org.mugiwaras.backend.model.serializer.DetalleJsonSerializer;
import org.mugiwaras.backend.model.serializer.OrdenCierreJsonSerializer;
import org.mugiwaras.backend.model.serializer.OrdenJsonSerializer;
import org.mugiwaras.backend.model.serializer.OrdenPassJsonSerializer;
import org.mugiwaras.backend.util.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_ORDEN)
public class OrdenController extends BaseRestController {

    @Autowired
    private IOrdenBusiness ordenBusiness;
    @Autowired
    private IDetalleBusiness detalleBusiness;

    @SneakyThrows
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(Long id) {
        return new ResponseEntity<>(ordenBusiness.load(id), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "/inicio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody String json) {
        StdSerializer<Orden> ser = new OrdenJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.add(json));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @SneakyThrows
    @PutMapping(value = "/checkin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkIn(@RequestBody String tara, @RequestHeader(name = "Numero-Orden") long numeroOrden) {
        StdSerializer<Orden> ser = new OrdenPassJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.checkIn(tara, numeroOrden));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @PostMapping(value = "/detalle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> detalles(@RequestBody Detalle detalle,
                                      @RequestHeader(name = "Numero-Orden") long numeroOrden,
                                      @RequestHeader(name = "Password") int password) {
        StdSerializer<Detalle> ser = new DetalleJsonSerializer(Detalle.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Detalle.class, ser, null).writeValueAsString(detalleBusiness.add(detalle, numeroOrden, password));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);}


    }

    @SneakyThrows
    @PostMapping(value = "/cierre/orden", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> close(@RequestHeader(name = "Numero-Orden") long numeroOrden) {
        StdSerializer<Orden> ser = new OrdenCierreJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.closeOrder(numeroOrden));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }


    @SneakyThrows
    @PutMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOut(@RequestBody String pesajeFinal, @RequestHeader(name = "Numero-Orden") long numeroOrden) {
        return new ResponseEntity<>(ordenBusiness.checkOut(pesajeFinal, numeroOrden), HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "/conciliacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOut(@RequestHeader(name = "Numero-Orden") long numeroOrden) {
        try {
            return new ResponseEntity<>(ordenBusiness.conciliacion(numeroOrden), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
