package org.mugiwaras.backend.controllers;

import lombok.SneakyThrows;
import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.business.interfaces.ICamionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_CAMION)
public class CamionController extends BaseRestController {

    @Autowired
    private ICamionBusiness camionBusiness;

    @SneakyThrows
    @GetMapping(value = "",  produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(Long id){
        return new ResponseEntity<>(camionBusiness.load(id), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "")
    public ResponseEntity<?> save(@RequestBody Camion camion){
        Camion response = camionBusiness.add(camion);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
