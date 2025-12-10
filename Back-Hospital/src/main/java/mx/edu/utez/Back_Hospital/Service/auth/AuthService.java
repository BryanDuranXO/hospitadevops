package mx.edu.utez.Back_Hospital.Service.auth;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.security.jwt.JwtProvider;
import mx.edu.utez.Back_Hospital.security.model.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    private final JwtProvider provider;
    private final AuthenticationManager manager;

    public AuthService(JwtProvider provider, AuthenticationManager manager) {
        this.provider = provider;
        this.manager = manager;
    }

    @Transactional
    public ResponseEntity<ApiResponse> signIn(String usuario, String password) {
        try {

            // SPRING SECURITY hará la autenticación usando UserDetailsServiceImpl
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario, password)
            );

            // Guardar autenticación
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar el token JWT
            String token = provider.generateToken(authentication);

            // Sacamos datos del usuario autenticado
            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("correo", principal.getUsername());
            responseData.put("roles", principal.getAuthorities());
            responseData.put("id", principal.getId());   // << AÑADIDO

            return new ResponseEntity<>(
                    new ApiResponse(responseData, HttpStatus.OK, "Token generado"),
                    HttpStatus.OK
            );

        } catch (Exception e) {
            e.printStackTrace();

            String message = "CredentialsMismatch";

            if (e instanceof DisabledException)
                message = "UserDisabled";

            if (e instanceof AccountExpiredException)
                message = "AccountExpired";

            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST, message, true),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}
