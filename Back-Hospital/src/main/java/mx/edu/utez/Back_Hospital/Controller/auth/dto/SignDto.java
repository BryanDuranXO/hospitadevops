package mx.edu.utez.Back_Hospital.Controller.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


public class SignDto {
    @NotBlank
    @NotEmpty
    private String usuario;
    @NotBlank
    @NotEmpty
    private String password;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
