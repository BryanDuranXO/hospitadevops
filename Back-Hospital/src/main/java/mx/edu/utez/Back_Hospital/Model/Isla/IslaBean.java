package mx.edu.utez.Back_Hospital.Model.Isla;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioBean;

import java.util.Set;

@Entity
@Table(name = "islas")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class IslaBean extends UsuarioBean {

    @Column(name = "numero")
    private String numero;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @OneToMany(mappedBy = "isla", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<EnfermeroBean> enfermeroBeanSet ;

    @OneToMany(mappedBy = "islaBean", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CamaBean> camaBeans ;

    public IslaBean(String numero, String token) {
        this.numero = numero;
        this.token = token;
    }

    public IslaBean() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<EnfermeroBean> getEnfermeroBeanSet() {
        return enfermeroBeanSet;
    }

    public void setEnfermeroBeanSet(Set<EnfermeroBean> enfermeroBeanSet) {
        this.enfermeroBeanSet = enfermeroBeanSet;
    }

    public Set<CamaBean> getCamaBeans() {
        return camaBeans;
    }

    public void setCamaBeans(Set<CamaBean> camaBeans) {
        this.camaBeans = camaBeans;
    }
}
