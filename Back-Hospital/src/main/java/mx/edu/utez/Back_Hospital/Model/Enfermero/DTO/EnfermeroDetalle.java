package mx.edu.utez.Back_Hospital.Model.Enfermero.DTO;

import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;

@Data
public class EnfermeroDetalle {
    private Long id;
    private String nombre;
    private String paterno;
    private String materno;
    private String telefono;

    public EnfermeroDetalle(EnfermeroBean e){
        this.id = e.getId();
        this.nombre = e.getNombre();
        this.paterno = e.getPaterno();
        this.materno = e.getMaterno();
        this.telefono = e.getTelefono();
    }
}
