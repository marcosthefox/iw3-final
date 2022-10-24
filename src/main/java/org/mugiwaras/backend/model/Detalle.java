package org.mugiwaras.backend.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "detalle")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Detalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "masa")
    private float masa;

    @Column(name = "densidad")
    private float densidad;

    @Column(name = "temperatura")
    private float temperatura;

    @Column(name = "caudal")
    private float caudal;

}
