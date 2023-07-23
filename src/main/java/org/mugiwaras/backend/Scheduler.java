package org.mugiwaras.backend;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.DetalleReciente;
import org.mugiwaras.backend.model.Orden;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.exceptions.NotFoundException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.mugiwaras.backend.model.business.interfaces.IOrdenBusiness;
import org.mugiwaras.backend.model.persistence.OrdenRepository;
import org.mugiwaras.backend.util.EmailBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
@Component
public class Scheduler {

    @Autowired
    private IDetalleBusiness detalleBusiness;

    @Autowired
    private IOrdenBusiness ordenBusiness;
    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private EmailBusiness emailBusiness;

    @Value("${temperaura.send.to:maliendo106@alumnos.iua.edu.ar}")
    private String temperaturaSendTo;

    @Value("${temperatura.umbral}")
    private float temperaturaUmbral;

    @Async
    @Scheduled(fixedDelayString = "${detalle.async:120}", initialDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void detalles() throws BusinessException, NotFoundException {
        log.info("Guardando los detalles...");

        // accedemos al Map que tiene <orden , detalle recientes>
        Map<Long, DetalleReciente> ordenes = detalleBusiness.getOrdenes();

        for (Map.Entry<Long, DetalleReciente> entry : ordenes.entrySet()) {
            Long ordenId = entry.getKey();
            DetalleReciente detalle = entry.getValue();

            detalleBusiness.add(detalle.getDetalleReciente(), ordenId);

            Orden orden = ordenBusiness.load(ordenId);
            if (!orden.isAlarma()){ // alarma no fue aceptada aun
                if (detalle.getDetalleReciente().getTemperatura() > temperaturaUmbral) {
                    String text="Temperatura umbral superada: " + detalle.getDetalleReciente().getTemperatura() + "\n";
                    emailBusiness.sendSimpleMessage(temperaturaSendTo, "Temperatura!", text);
                }
                orden.setAlarma(true);
                ordenRepository.save(orden);
            }
        }

        detalleBusiness.borrarMapaOrdenes();
    }
}
