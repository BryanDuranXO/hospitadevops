package mx.edu.utez.Back_Hospital.Model.Paciente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<PacienteBean, Long> {

     Optional<PacienteBean> findByCurp(String curp);
//    Optional<PacienteBean> findByUsuario(String usuario);
//
//    String usuario(String usuario);
}
