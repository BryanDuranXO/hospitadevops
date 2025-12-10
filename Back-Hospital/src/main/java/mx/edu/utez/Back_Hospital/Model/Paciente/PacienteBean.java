package mx.edu.utez.Back_Hospital.Model.Paciente;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioBean;

import java.util.List;

@Entity
@Table(name = "pacientes")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class PacienteBean extends UsuarioBean {


    @Column(name = "padecimientos", columnDefinition = "TEXT")
    private String padecimientos;

    @Column(name = "curp", length = 18)
    private String curp;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "alta", columnDefinition = "BOOL")
    private Boolean alta;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pacientes_Camas> pacientesCamas;

    public PacienteBean(String padecimientos, String curp, String token, Boolean alta, List<Pacientes_Camas> pacientesCamas) {
        this.padecimientos = padecimientos;
        this.curp = curp;
        this.token = token;
        this.alta = alta;
        this.pacientesCamas = pacientesCamas;
    }

    public PacienteBean(String padecimientos, String curp, String token, Boolean alta) {
        this.padecimientos = padecimientos;
        this.curp = curp;
        this.token = token;
        this.alta = alta;
    }

    public PacienteBean() {}

    public String getPadecimientos() {
        return padecimientos;
    }

    public void setPadecimientos(String padecimientos) {
        this.padecimientos = padecimientos;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public List<Pacientes_Camas> getPacientesCamas() {
        return pacientesCamas;
    }

    public void setPacientesCamas(List<Pacientes_Camas> pacientesCamas) {
        this.pacientesCamas = pacientesCamas;
    }
}
