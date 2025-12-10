package mx.edu.utez.Back_Hospital.Model.Enfermero.DTO;

import lombok.Data;

@Data
public class DTOPatch_Token {
    private Long id;
    private String token;

    public DTOPatch_Token() {
    }

    public DTOPatch_Token(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
