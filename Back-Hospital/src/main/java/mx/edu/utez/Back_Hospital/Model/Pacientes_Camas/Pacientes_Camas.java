package mx.edu.utez.Back_Hospital.Model.Pacientes_Camas;

import jakarta.persistence.*;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;

@Entity
@Table(name = "pacientes_camas")
public class Pacientes_Camas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private PacienteBean paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cama", nullable = false)
    private CamaBean cama;

    @Column(columnDefinition = "BOOL")
    private boolean activo = true;

    public Pacientes_Camas() {
    }

    public Pacientes_Camas(PacienteBean paciente, CamaBean cama, boolean activo) {
        this.paciente = paciente;
        this.cama = cama;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public CamaBean getCama() {
        return cama;
    }

    public void setCama(CamaBean cama) {
        this.cama = cama;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
