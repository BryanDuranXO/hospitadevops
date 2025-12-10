package mx.edu.utez.Back_Hospital.Model.Cama;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_Camas;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;

import java.util.List;

@Entity
@Table(name = "camas")
public class CamaBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cama")
    private int cama;

    @Column(name = "ocupada", columnDefinition = "BOOL")
    private boolean ocupada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_isla", nullable = false)

    private IslaBean islaBean;

    @OneToMany(mappedBy = "cama", fetch = FetchType.LAZY)
    @JsonIgnore
    private java.util.List<Enfermeros_Camas> asignacionesEnfermeros;

    @OneToMany(mappedBy = "cama", fetch = FetchType.LAZY)
    @JsonIgnore
    private java.util.List<Pacientes_Camas> pacientesCamas;

    public CamaBean() {
    }

    public CamaBean(int cama, boolean ocupada, IslaBean islaBean) {
        this.cama = cama;
        this.ocupada = ocupada;
        this.islaBean = islaBean;
    }

    public CamaBean(Long id, int cama, boolean ocupada, IslaBean islaBean) {
        this.id = id;
        this.cama = cama;
        this.ocupada = ocupada;
        this.islaBean = islaBean;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCama() {
        return cama;
    }

    public void setCama(int cama) {
        this.cama = cama;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public IslaBean getIslaBean() {
        return islaBean;
    }

    public void setIslaBean(IslaBean islaBean) {
        this.islaBean = islaBean;
    }

    public List<Enfermeros_Camas> getAsignacionesEnfermeros() {
        return asignacionesEnfermeros;
    }

    public void setAsignacionesEnfermeros(List<Enfermeros_Camas> asignacionesEnfermeros) {
        this.asignacionesEnfermeros = asignacionesEnfermeros;
    }

    public List<Pacientes_Camas> getPacientesCamas() {
        return pacientesCamas;
    }

    public void setPacientesCamas(List<Pacientes_Camas> pacientesCamas) {
        this.pacientesCamas = pacientesCamas;
    }
}
