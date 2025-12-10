package mx.edu.utez.Back_Hospital.Model.Rol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<RolBean, Long> {

    boolean existsByRol(String rol);

    Optional<RolBean> findByRol(String rol);
}
