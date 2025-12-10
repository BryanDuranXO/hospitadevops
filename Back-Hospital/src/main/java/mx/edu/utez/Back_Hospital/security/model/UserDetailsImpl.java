package mx.edu.utez.Back_Hospital.security.model;

import lombok.Getter;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;

import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
public class UserDetailsImpl implements UserDetails {

    private Long id;  // <<< AÑADIDO
    private String username;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, String password, boolean enabled,
                           Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public UserDetailsImpl(Long id,String username, String password, boolean enabled,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }



    public UserDetailsImpl(String username, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = true;
    }


    /* ============================================================
     *    1) BUILD PARA PACIENTE
     * ============================================================ */
    public static UserDetailsImpl buildPaciente(UsuarioBean user) {
        Set<GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_PACIENTE"));

        return new UserDetailsImpl(
                user.getId(),         // << EL ID REAL DEL USUARIO
                user.getUsuario(),          // username
                user.getPassword(),        // contrasenia o campo equivalente
                true,                      // enabled (puedes cambiar si tienes campo)
                authorities
        );
    }

    public static UserDetailsImpl buildEnfermero(UsuarioBean user) {
        Set<GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ENFERMERO"));

        return new UserDetailsImpl(
                user.getId(),         // << EL ID REAL DEL USUARIO
                user.getUsuario(),
                user.getPassword(),
                user.isStatus(),
                authorities
        );
    }

    public static UserDetailsImpl buildIsla(UsuarioBean user) {
        Set<GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ISLA"));

        return new UserDetailsImpl(
                user.getId(),         // << EL ID REAL DEL USUARIO
                user.getUsuario(),
                user.getPassword(),
                user.isStatus(),
                authorities
        );
    }

    public static UserDetailsImpl buildGeneric(UsuarioBean user) {
        // Usa el rol que venga desde la BD
        Set<GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority(user.getRol().getRol()));

        return new UserDetailsImpl(
                user.getId(),         // << EL ID REAL DEL USUARIO
                user.getUsuario(),
                user.getPassword(),
                user.isStatus(),
                authorities
        );
    }










    /* ============================================================
     *    MÉTODOS DE UserDetails
     * ============================================================ */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
}
