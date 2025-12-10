package mx.edu.utez.Back_Hospital.Model.Usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioBean, Long> {

    Optional<UsuarioBean> findByUsuario(String username);
}
