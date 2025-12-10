package mx.edu.utez.Back_Hospital.Model.Usuarios;

import jakarta.annotation.PostConstruct;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaRepository;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;
import mx.edu.utez.Back_Hospital.Model.Rol.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InserIsla {
    @Autowired
    private IslaRepository islaRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // evita duplicados
        if (!islaRepository.existsByUsuario("isla1")) {

            RolBean rol = rolRepository.findByRol("ISLA").orElse(null);
            if (rol == null) return;

            IslaBean isla = new IslaBean();
            isla.setUsuario("isla1");
            isla.setPassword(passwordEncoder.encode("12345"));   // puedes encriptarlo
            isla.setRol(rol);
            isla.setNumero("1");
            isla.setToken(null);
            isla.setStatus(true);

            islaRepository.save(isla);
        }
    }
}
