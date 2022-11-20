package org.mugiwaras.backend.controllers;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.Cliente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IClienteBusiness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_CLIENTE)
@RequiredArgsConstructor
public class ClienteController extends BaseRestController{

    private final IClienteBusiness clienteBusiness;

    @SneakyThrows
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Cliente cliente) {
        try {
            Cliente result = clienteBusiness.add(cliente);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_CLIENTE + "/buscar/" + result.getRazonSocial());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SneakyThrows
    @GetMapping(value = "/buscar/{razonsocial}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable("razonsocial") long razonSocial){
        try {
            return new ResponseEntity<>(clienteBusiness.load(razonSocial), HttpStatus.OK);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }



}
