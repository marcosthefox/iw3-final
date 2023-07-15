package org.mugiwaras.backend.controllers;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mugiwaras.backend.controllers.constants.Constants;
import org.mugiwaras.backend.model.Detalle;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.FoundException;
import org.mugiwaras.backend.model.business.exceptions.NotAuthorizedException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
import org.mugiwaras.backend.model.serializer.*;
import org.mugiwaras.backend.util.JsonUtiles;
import org.mugiwaras.backend.util.StandartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_ORDEN)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios de la entidad Orden", name = "Orden")
@RequiredArgsConstructor
public class OrdenController extends BaseRestController {


    @Autowired
    private IOrdenBusiness ordenBusiness;
    @Autowired
    private IDetalleBusiness detalleBusiness;

    @SneakyThrows
    @Hidden //TODO: *********** esto se usa?? **************************
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(Long id) {
        return new ResponseEntity<>(ordenBusiness.load(id), HttpStatus.OK);
    }

    @SneakyThrows
    @Operation(operationId = "inicio", summary = "Este servicio crea una Orden. (1)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Orden.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping(value = "/inicio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Orden orden) {
        StdSerializer<Orden> ser = new OrdenJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.add(orden));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SneakyThrows
    @Operation(operationId = "b2b", summary = "Este servicio crea una Orden a partir de codigos externos de las entidades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Orden.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping(value = "/b2b", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addExternal(@RequestBody String json) {
        StdSerializer<Orden> ser = new OrdenJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.addExternal(json));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SneakyThrows
    @Operation(operationId = "checkin", summary = "Este servicio hace el check-in de una Orden. (2)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna una password."),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
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
    @Operation(operationId = "detalle", summary = "Este servicio provee el detalle de una medicion. (3)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle generado correctamente"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
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
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @Operation(operationId = "cierre-orden", summary = "Este servicio cierra una orden. (4)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden cerrada corectamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Orden.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
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
    @Operation(operationId = "checkout", summary = "Este servicio hace el checkout de una orden. (5)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout realizado correctamente"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOut(@RequestBody String pesajeFinal, @RequestHeader(name = "Numero-Orden") long numeroOrden) {
        return new ResponseEntity<>(ordenBusiness.checkOut(pesajeFinal, numeroOrden), HttpStatus.OK);
    }

    @SneakyThrows
    @Operation(operationId = "conciliacion", summary = "Este servicio devuelve una conciliacion de la orden. (6)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conciliacion retornada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ConciliacionSerializer.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @GetMapping(value = "/conciliacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOut(@RequestHeader(name = "Numero-Orden") long numeroOrden) {
        try {
            return new ResponseEntity<>(ordenBusiness.conciliacion(numeroOrden), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
