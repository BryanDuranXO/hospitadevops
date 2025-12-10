package mx.edu.utez.Back_Hospital.Model.Isla;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IslaRepository extends JpaRepository<IslaBean,Long> {
    Optional<IslaBean> findByNumero(String numero);

    @Query("SELECT i FROM IslaBean i " +
            "LEFT JOIN FETCH i.enfermeroBeanSet " +
            "LEFT JOIN FETCH i.camaBeans " +
            "WHERE i.id = :idIsla")
    Optional<IslaBean> findIslaWithEnfermerosAndCamas(Long idIsla);

    boolean existsByUsuario(String usuario);

}
