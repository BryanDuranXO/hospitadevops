package mx.edu.utez.Back_Hospital.Model.Cama.DTO;

import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;

@Data
public class DTOCamaSimple {
    private Long id;
    private int cama;
    private boolean ocupada;

    public DTOCamaSimple(CamaBean cama) {
        this.id = cama.getId();
        this.cama = cama.getCama();
        this.ocupada = cama.isOcupada();
    }


}
