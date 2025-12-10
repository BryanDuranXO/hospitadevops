package mx.edu.utez.Back_Hospital.Model.Cama;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CamaRepository extends JpaRepository<CamaBean,Long> {
    Optional<CamaBean> findByCama(int cama);

    @Query("SELECT c FROM CamaBean c WHERE c.islaBean.id = :idIsla AND c.ocupada = false")
    List<CamaBean> findCamasDisponiblesByIsla(Long idIsla);


}

