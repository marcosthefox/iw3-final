package org.mugiwaras.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "camion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Camion implements Serializable {


    private static final long serialVersionUID = -2162240618874701205L;

    @Hidden
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_camion;

    @Column(unique = true)
    private String patente;

    @Column
    private String descripcion;


    @OneToMany(mappedBy = "camion",fetch = FetchType.EAGER)
    List<Cisternado> datosCisterna;

    @Column
    private long totalCisterna;

    @Column(length = 50, nullable = false, unique = true)
    private String code; //codigo externo

}
