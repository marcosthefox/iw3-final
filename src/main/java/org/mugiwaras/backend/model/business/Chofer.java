package org.mugiwaras.backend.model.business;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "chofer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chofer implements Serializable {


    private static final long serialVersionUID = -2600344777206015479L;

    @Id
    private long dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;
}
