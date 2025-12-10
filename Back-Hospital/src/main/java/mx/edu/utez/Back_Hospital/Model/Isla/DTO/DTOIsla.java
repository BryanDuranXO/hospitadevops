package mx.edu.utez.Back_Hospital.Model.Isla.DTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.DTO.DTOEnfermero;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;

import java.util.Set;

@Data
public class DTOIsla {

    @NotNull(groups = {DTOIsla.Modify.class})
    private Long id;

    @NotBlank(groups = {DTOIsla.Register.class, DTOIsla.Modify.class})
    private String usuario;

    @NotBlank(groups = {DTOIsla.Register.class, DTOIsla.Modify.class})
    private String password;

    @NotNull(groups = {DTOIsla.Register.class, DTOIsla.Modify.class})
    private Long idRol;

    @NotBlank(groups = {DTOIsla.Register.class, DTOIsla.Modify.class})
    private String numero;

    @NotBlank(groups = {DTOIsla.Register.class, DTOIsla.Modify.class})
    private String token;

    @NotNull(groups = {DTOIsla.Register.class, DTOIsla.Modify.class})
    private Boolean status;

    private Set<EnfermeroBean> enfermeroBeanSet ;

    private Set<CamaBean> camaBeans ;

    public DTOIsla() {
    }

    public IslaBean toEntity(RolBean rol) {
        IslaBean isla = new IslaBean();

        isla.setUsuario(usuario);
        isla.setPassword(password);
        isla.setRol(rol);
        isla.setNumero(numero);
        isla.setToken(token);
        isla.setStatus(status);
        return isla;
    }

    public DTOIsla(IslaBean isla){
        this.id = isla.getId();
        this.numero = isla.getNumero();
    }


    public interface Register{}
    public interface Modify{}
    public interface ChangeStatus{}

}
