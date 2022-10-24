package org.mugiwaras.backend.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "cisternado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cisternado implements Serializable {


    private static final long serialVersionUID = -1804488154297204890L;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_camion")
    private Camion camion;

    @Column
    private long tamanio;
}
