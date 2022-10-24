package org.mugiwaras.backend.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto implements Serializable {


    private static final long serialVersionUID = -2510094698283791123L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

}
