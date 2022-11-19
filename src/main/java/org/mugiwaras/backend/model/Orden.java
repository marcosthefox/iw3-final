package org.mugiwaras.backend.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "orden")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orden implements Serializable {

    @Id
    private long numeroOrden;

    @Column(nullable = false , unique = true)
    private String CodigoExterno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_camion")
    private Camion camion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_chofer")
    private Chofer chofer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @OneToMany(mappedBy = "orden",fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Detalle> detalle;

    //VER FECHAS!!!!
    @Column(name = "preset")
    private Float preset;

    @Column(name = "fecha_turno_carga")
    private OffsetDateTime fechaTurnoCarga;

    @Column(name = "fecha_pesaje_inicial")
    private OffsetDateTime fechaPesajeInicial;

    @Column(name = "estado")
    private int estado;

    @Column(name = "tara")
    private long tara;

    @Column(name = "password")
    private long password;

    @Column(name = "pesaje_final")
    private long pesajeFinal;


    @Column(name = "fecha_pesaje_final")
    private OffsetDateTime fechaPesajeFinal;

    @Column(name = "fecha_detalle_final")
    private OffsetDateTime fechaDetalleFinal;

    @Column(name = "fecha_detalle_inicial")
    private OffsetDateTime fechaDetalleInicial;




}
