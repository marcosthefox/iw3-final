package org.mugiwaras.backend;

import lombok.extern.slf4j.Slf4j;
import org.mugiwaras.backend.model.DetalleReciente;
import org.mugiwaras.backend.model.business.exceptions.BusinessException;
import org.mugiwaras.backend.model.business.interfaces.IDetalleBusiness;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Async
    @Scheduled(fixedDelayString = "${detalle.async:120}", initialDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void detalles() throws BusinessException {
        log.info("Guardando los detalles...");

        // accedemos al Map de que tiene <orden,detalle recientes>
        Map<Long, DetalleReciente> ordenes = detalleBusiness.getOrdenes();

        for (Map.Entry<Long, DetalleReciente> entry : ordenes.entrySet()) {
            Long ordenId = entry.getKey();
            DetalleReciente detalle = entry.getValue();

            detalleBusiness.add(detalle.getDetalleReciente(), ordenId);
        }

        detalleBusiness.borrarMapaOrdenes();
    }
}
