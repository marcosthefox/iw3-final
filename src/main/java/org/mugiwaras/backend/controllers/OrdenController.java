package org.mugiwaras.backend.controllers;

import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.interfaces.ICamionBusiness;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
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

    @SneakyThrows
    @GetMapping(value = "",  produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(Long id){
        return new ResponseEntity<>(ordenBusiness.load(id), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "/inicio")
    public ResponseEntity<?> save(@RequestBody Orden orden){
        ordenBusiness.add(orden);
        String response = "Se creo la orden bro segui asi";
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @SneakyThrows
    @PutMapping(value = "/checkin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkIn(@RequestBody Orden orden){
        return new ResponseEntity<>(ordenBusiness.checkIn(orden), HttpStatus.OK);
    }
}
