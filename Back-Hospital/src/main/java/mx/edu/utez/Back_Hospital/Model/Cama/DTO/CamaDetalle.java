package mx.edu.utez.Back_Hospital.Model.Cama.DTO;

import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;

@Data
public class CamaDetalle {
    private Long id;
    private int cama;
    private boolean ocupada;

    public CamaDetalle(CamaBean c){
        this.id = c.getId();
        this.cama = c.getCama();
        this.ocupada = c.isOcupada();
    }
}
