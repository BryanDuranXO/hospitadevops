package mx.edu.utez.Back_Hospital.Model.Isla.DTO;

import lombok.Data;

@Data
public class DtoPatchToken {
    private Long id;
    private String token;

    public DtoPatchToken() {
    }

    public DtoPatchToken(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
