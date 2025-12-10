package mx.edu.utez.Back_Hospital.security.service;



import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroRepository;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaRepository;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteRepository;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioBean;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioRepository;
import mx.edu.utez.Back_Hospital.security.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {

        UsuarioBean user = usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Detectar tipo real (Paciente, Enfermero o Isla)
        if (user instanceof PacienteBean paciente) {
            return UserDetailsImpl.buildPaciente(paciente);
        }

        if (user instanceof EnfermeroBean enfermero) {
            return UserDetailsImpl.buildEnfermero(enfermero);
        }

        if (user instanceof IslaBean isla) {
            return UserDetailsImpl.buildIsla(isla);
        }

        // Si llega aquí, es un usuario genérico
        return UserDetailsImpl.buildGeneric(user);
    }
}
