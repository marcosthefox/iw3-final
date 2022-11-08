package org.mugiwaras.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_cisterna;

    @ManyToOne
    @JoinColumn(name = "id_camion")
    @JsonIgnore
    private Camion camion;

    @Column
    private long tamanio;
}
