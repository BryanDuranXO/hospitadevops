package mx.edu.utez.Back_Hospital.Model.Paciente.DTO;

import lombok.Data;

@Data
public class DTOPaciente_PatchToken {
    private Long id;
    private String token;

    public DTOPaciente_PatchToken() {
    }

    public DTOPaciente_PatchToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
