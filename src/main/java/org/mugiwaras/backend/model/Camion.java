package org.mugiwaras.backend.model;


import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "camion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Camion implements Serializable {


    private static final long serialVersionUID = -2162240618874701205L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String patente;

    @Column
    private String descripcion;


    @OneToMany(mappedBy = "camion",fetch = FetchType.EAGER)
    ArrayList<Cisternado> datosCisterna;

    @Column
    private long totalCisterna;

}
