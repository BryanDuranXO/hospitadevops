package mx.edu.utez.Back_Hospital.Model.Enfermero;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_Camas;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioBean;

import java.util.List;

@Entity
@Table(name = "enfermeros")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class EnfermeroBean extends UsuarioBean {


    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_isla", nullable = false)
    private IslaBean isla;

    @OneToMany(mappedBy = "enfermero", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Enfermeros_Camas> asignacionesCamas;

    public EnfermeroBean(String token, IslaBean isla, List<Enfermeros_Camas> asignacionesCamas) {
        this.token = token;
        this.isla = isla;
        this.asignacionesCamas = asignacionesCamas;
    }

    public EnfermeroBean(String token) {
        this.token = token;
    }

    public EnfermeroBean() {}


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public IslaBean getIsla() {
        return isla;
    }

    public void setIsla(IslaBean isla) {
        this.isla = isla;
    }

    public List<Enfermeros_Camas> getAsignacionesCamas() {
        return asignacionesCamas;
    }

    public void setAsignacionesCamas(List<Enfermeros_Camas> asignacionesCamas) {
        this.asignacionesCamas = asignacionesCamas;
    }
}
