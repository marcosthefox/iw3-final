package org.mugiwaras.backend.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.Cliente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IClienteBusiness;
import org.mugiwaras.backend.util.StandartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_CLIENTE)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios de la entidad Cliente", name = "Cliente")
@RequiredArgsConstructor
public class ClienteController extends BaseRestController {

    @Autowired
    private IClienteBusiness clienteBusiness;

    @SneakyThrows
    @Operation(operationId = "cliente-post", summary = "Este servicio crea un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente."),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Cliente cliente) {
        try {
            Cliente result = clienteBusiness.add(cliente);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_CLIENTE + "/buscar/" + result.getCode());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SneakyThrows
    @Operation(operationId = "cliente-get", summary = "Este servicio busca un cliente a partir de su codigo externo.")
    @Parameter(in = ParameterIn.PATH, name = "code", schema = @Schema(type = "string"), required = true, description = "Codigo externo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se encontro el cliente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra el cliente para el identificador informado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping(value = "/buscar/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(clienteBusiness.load(code), HttpStatus.OK);
        } catch (FoundException | BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
