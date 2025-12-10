package mx.edu.utez.Back_Hospital.Model.Isla.DTO;

import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Cama.DTO.CamaDetalle;
import mx.edu.utez.Back_Hospital.Model.Enfermero.DTO.EnfermeroDetalle;

import java.util.List;

@Data
public class DetalleIslaDTO {

    private Long id;
    private String numero;
    private String token;

    private List<EnfermeroDetalle> enfermeros;
    private List<CamaDetalle> camas;

    public DetalleIslaDTO(Long id, String numero, String token,
                         List<EnfermeroDetalle> enfermeros,
                         List<CamaDetalle> camas) {
        this.id = id;
        this.numero = numero;
        this.token = token;
        this.enfermeros = enfermeros;
        this.camas = camas;
    }

    public DetalleIslaDTO() {}

}
