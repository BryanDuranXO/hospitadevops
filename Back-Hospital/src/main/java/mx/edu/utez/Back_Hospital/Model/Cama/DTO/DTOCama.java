package mx.edu.utez.Back_Hospital.Model.Cama.DTO;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Isla.DTO.DTOIsla;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;

@Data
public class DTOCama {
    private Long id;

    private int cama;

    private boolean ocupada;

    private DTOIsla isla;

    private Long islaId; // Para recibir el ID de la isla en POST

    public DTOCama() {}

    public CamaBean toEntity() {
        IslaBean isla = new IslaBean();
        isla.setId(islaId);
        return new CamaBean(cama, ocupada, isla);
    }

    public DTOCama(CamaBean cama){
        this.id = cama.getId();
        this.cama = cama.getCama();
        this.ocupada = cama.isOcupada();
        this.isla = new DTOIsla(cama.getIslaBean());
    }



}
