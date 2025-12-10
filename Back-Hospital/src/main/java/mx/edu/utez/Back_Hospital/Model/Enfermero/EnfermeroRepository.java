package mx.edu.utez.Back_Hospital.Model.Enfermero;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnfermeroRepository extends JpaRepository<EnfermeroBean,Long> {

    Optional<EnfermeroBean> findByUsuario(String usuario);
    Optional<EnfermeroBean> findByTelefono(String telefono);

    @Query("SELECT e FROM EnfermeroBean e WHERE e.isla.id = :idIsla")
    List<EnfermeroBean> findByIsla(Long idIsla);

}
