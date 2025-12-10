package mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.DTO;

import jakarta.persistence.*;
import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;

import java.util.function.LongFunction;

@Data
public class DTOPacientes_Camas {
    private Long id;

    private Long pacienteId;

    private Long camaId;

    private boolean activo = true;

    public Pacientes_Camas toEntity() {

        Pacientes_Camas pc = new Pacientes_Camas();

        PacienteBean paciente = new PacienteBean();
        paciente.setId(this.pacienteId);

        CamaBean cama = new CamaBean();
        cama.setId(this.camaId);

        pc.setPaciente(paciente);
        pc.setCama(cama);

        return pc;
    }
}
