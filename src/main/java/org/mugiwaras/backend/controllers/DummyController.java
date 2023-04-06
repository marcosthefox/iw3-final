package org.mugiwaras.backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.URL_DUMMY)
@RequiredArgsConstructor
public class DummyController extends BaseRestController {

    @SneakyThrows
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get() {

        return new ResponseEntity<>("Dummy Response", HttpStatus.OK);
    }
}
