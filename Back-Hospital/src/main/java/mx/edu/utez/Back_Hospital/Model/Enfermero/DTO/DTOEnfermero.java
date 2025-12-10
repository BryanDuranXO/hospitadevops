package mx.edu.utez.Back_Hospital.Model.Enfermero.DTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_Camas;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.DTO.DTOPaciente;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;

import java.util.List;

@Data
public class DTOEnfermero {
    @NotNull(groups = {DTOEnfermero.Modify.class})
    private Long id;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String nombre;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String paterno;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String materno;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String telefono;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String usuario;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String password;

    @NotNull(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private Long idRol;

    @NotBlank(groups = {DTOEnfermero.Register.class, DTOEnfermero.Modify.class})
    private String token;

    private IslaBean isla;

    private List<Enfermeros_Camas> asignacionesCamas;

    public EnfermeroBean toEntity(RolBean rol) {
        EnfermeroBean enfermero = new EnfermeroBean();

        // ==== CAMPOS HEREDADOS DE UsuarioBean ====
        enfermero.setId(this.id);
        enfermero.setNombre(this.nombre);
        enfermero.setPaterno(this.paterno);
        enfermero.setMaterno(this.materno);
        enfermero.setTelefono(this.telefono);
        enfermero.setUsuario(this.usuario);
        enfermero.setPassword(this.password); // el service la encripta
        enfermero.setStatus(true); // opcional, si lo pones en el DTO úsalo
        enfermero.setRol(rol);

        // ==== CAMPOS PROPIOS DE EnfermeroBean ====
        enfermero.setToken(this.token);
      enfermero.setIsla(this.isla);
        enfermero.setAsignacionesCamas(this.asignacionesCamas);

        return enfermero;
    }

    public interface Register{}
    public interface Modify{}
    public interface ChangeStatus{}

}
