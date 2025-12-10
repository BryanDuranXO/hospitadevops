package mx.edu.utez.Back_Hospital.Model.Rol;

import jakarta.persistence.*;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;

import java.util.Set;

@Entity
@Table(name = "roles")
public class RolBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rol")
    private String rol;

//    @OneToMany(mappedBy = "rolenfermero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<EnfermeroBean> enfermeroBeanSet ;
//
//    @OneToMany(mappedBy = "rolpaciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<PacienteBean> pacienteBeanSet ;
//
//    @OneToMany(mappedBy = "rolisla", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<IslaBean> islaBeans ;


    public RolBean() {
    }

    public RolBean(String rol) {
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
