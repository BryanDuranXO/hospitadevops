package mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas;

import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Enfermeros_CamasRepository extends JpaRepository<Enfermeros_Camas, Long> {
    Optional<Enfermeros_Camas> findByActivo(Boolean activo);

    @Query("SELECT ec.cama FROM Enfermeros_Camas ec WHERE ec.enfermero.id = :idEnfermero AND ec.activo = true")
    List<CamaBean> findCamasByEnfermero(Long idEnfermero);

    @Query("SELECT COUNT(ec) FROM Enfermeros_Camas ec WHERE ec.enfermero.id = :idEnfermero")
    int countCamasAsignadas(Long idEnfermero);

    Optional<Enfermeros_Camas> findByCamaIdAndActivoTrue(Long idCama);



}
