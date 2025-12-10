package mx.edu.utez.Back_Hospital.Model.Paciente.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;

import java.util.List;

@Data
public class DTOPaciente {

    @NotNull(groups = {Modify.class})
    private Long id;

    @NotBlank(groups = {Register.class, Modify.class})
    private String nombre;

    @NotBlank(groups = {Register.class, Modify.class})
    private String paterno;

    @NotBlank(groups = {Register.class, Modify.class})
    private String materno;

    @NotBlank(groups = {Register.class, Modify.class})
    private String telefono;

    @NotBlank(groups = {Register.class, Modify.class})
    private String usuario;

    @NotBlank(groups = {Register.class, Modify.class})
    private String password;

    @NotNull(groups = {Register.class, Modify.class})
    private Long idRol;

    @NotBlank(groups = {Register.class, Modify.class})
    private String padecimientos;

    @NotBlank(groups = {Register.class, Modify.class})
    private String curp;

    @NotBlank(groups = {Register.class, Modify.class})
    private String token;

    @NotNull(groups = {Register.class, Modify.class})
    private Boolean alta;

    @NotNull(groups = {Register.class, Modify.class})
    private Boolean status;

    private List<Pacientes_Camas> pacientesCamas;

    public PacienteBean toEntity(RolBean rol) {
        PacienteBean paciente = new PacienteBean();

        paciente.setNombre(this.nombre);
        paciente.setPaterno(this.paterno);
        paciente.setMaterno(this.materno);
        paciente.setTelefono(this.telefono);
        paciente.setUsuario(this.usuario);
        paciente.setPassword(this.password); // Se encripta en el service
        paciente.setStatus(this.status);
        paciente.setRol(rol);

        paciente.setPadecimientos(this.padecimientos);
        paciente.setCurp(this.curp);
        paciente.setToken(this.token);
        paciente.setAlta(this.alta);
        paciente.setPacientesCamas(this.pacientesCamas);

        return paciente;
    }


    public interface Register{}
    public interface Modify{}
    public interface ChangeStatus{}
}
