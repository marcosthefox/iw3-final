package org.mugiwaras.backend.controllers;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.Camion;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.ICamionBusiness;
import org.mugiwaras.backend.model.serializer.OrdenJsonSerializer;
import org.mugiwaras.backend.util.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_CAMION)
@RequiredArgsConstructor
public class CamionController extends BaseRestController{

    @Autowired
    private  ICamionBusiness camionBusiness;


   /* @GetMapping(value = "/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ( String slimVersion, @PathVariable("number") Long number) {
        try {
            StdSerializer<Bill> ser = null;
            if (slimVersion.equalsIgnoreCase("v1")) {
                ser = new BillSlimV1JsonSerializer(Bill.class, false);
                String result = JsonUtiles.getObjectMapper(Bill.class, ser, null).writeValueAsString(billBusiness.loadByNumberV1(number));
                return new ResponseEntity<>(result, HttpStatus.OK);//return v1

            } else if (slimVersion.equalsIgnoreCase("v2")) {
                ser = new BillSlimV2JsonSerializer(Bill.class, false);
                String result = JsonUtiles.getObjectMapper(Bill.class, ser, null).writeValueAsString(billBusiness.loadByNumberV2(number));
                return new ResponseEntity<>(result, HttpStatus.OK);//return v2
            } else {
                return new ResponseEntity<>(billBusiness.loadByNumberV1(number), HttpStatus.OK); //return v0
            }

        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException | JsonProcessingException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }

        return null;
    }*/

    @SneakyThrows
    @GetMapping(value = "/buscar/{patente}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable("patente") String patente){
        try {
            return new ResponseEntity<>(camionBusiness.load(patente), HttpStatus.OK);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e){
         return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }







    @SneakyThrows
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Camion camion) {
        try {
            Camion result = camionBusiness.add(camion);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_CAMION + "/buscar/" + result.getPatente());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}
