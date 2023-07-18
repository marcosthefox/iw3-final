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
import org.mugiwaras.backend.model.serializer.DetalleJsonSerializer;
import org.mugiwaras.backend.model.serializer.OrdenCierreJsonSerializer;
import org.mugiwaras.backend.model.serializer.OrdenJsonSerializer;
import org.mugiwaras.backend.model.serializer.OrdenPassJsonSerializer;
import org.mugiwaras.backend.util.JsonUtiles;
import org.mugiwaras.backend.util.StandartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_ORDEN)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios de la entidad Orden. Es necesario tener ROLE_ADMIN.", name = "Orden")
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
    @Operation(operationId = "inicio", summary = "(1) Este servicio crea una Orden. Deja la orden en ESTADO 1.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Orden.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @Operation(operationId = "b2b", summary = "Este servicio crea una Orden a partir de codigos externos de las entidades. Deja la orden en ESTADO 1.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada correctamente.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Orden.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/b2b", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addExternal(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Entidades:  numeroOrden, camion, chofer, cliente, producto, preset, fechaTurnoCarga.")
                                         @RequestBody String json) {
        StdSerializer<Orden> ser = new OrdenJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.addExternal(json));
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @SneakyThrows
    @Operation(operationId = "checkin", summary = "(2) Este servicio hace el check-in de una Orden. Deja la orden en ESTADO 2.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna una password."),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/checkin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkIn(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Proveer el pesaje_inicial (int).")
                                     @RequestBody String tara,
                                     @RequestHeader(name = "Numero-Orden") long numeroOrden) {
        StdSerializer<Orden> ser = new OrdenPassJsonSerializer(Orden.class, false);
        try {
            String result = JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(ordenBusiness.checkIn(tara, numeroOrden));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @Operation(operationId = "detalle", summary = "(3) Este servicio provee el detalle de una medicion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle generado correctamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Detalle.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @Operation(operationId = "cierre-orden", summary = "(4) Este servicio cierra una orden. Deja la orden en ESTADO 3.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden cerrada corectamente."),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @Operation(operationId = "checkout", summary = "(5) Este servicio hace el checkout de una orden y recibe el pesaje final del camion. Deja la orden en ESTADO 4.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout realizado correctamente"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOut(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Proveer el pesaje_final (int).")
                                      @RequestBody String pesajeFinal,
                                      @RequestHeader(name = "Numero-Orden") long numeroOrden) {
        return new ResponseEntity<>(ordenBusiness.checkOut(pesajeFinal, numeroOrden), HttpStatus.OK);
    }

    @SneakyThrows
    @Operation(operationId = "conciliacion", summary = "(6) Este servicio devuelve una conciliacion de la orden. Solo para ordenes en estado 4.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conciliacion retornada correctamente."),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/conciliacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOut(@RequestHeader(name = "Numero-Orden") long numeroOrden) {
        try {
            return new ResponseEntity<>(ordenBusiness.conciliacion(numeroOrden), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
