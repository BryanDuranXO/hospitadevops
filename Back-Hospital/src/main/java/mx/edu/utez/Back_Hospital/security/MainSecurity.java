package mx.edu.utez.Back_Hospital.security;

import mx.edu.utez.Back_Hospital.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MainSecurity {

    private final String[] WHITE_LIST = {
            "/api/auth/**",
            "/api/qr/**",
            "/api/isla/patch-token",
            "/api/paciente/patch-token",
            "/api/enfermero/patch-token",
            "/api/notifications/send",
            "/api/alertas/solicitar",
            "/api/isla/new-isla"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter filter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers("/api/enfermero/new-enfermero").hasAnyRole("ISLA")
                                .requestMatchers("/api/isla/").hasAnyRole("ISLA")
                                .requestMatchers("/api/paciente/save").hasAnyRole("ISLA")
                                .requestMatchers("/api/cama/").hasAnyRole("ISLA")
                                .requestMatchers("/api/cama/new-cama").hasAnyRole("ISLA")
                                .requestMatchers("/api/e-c/").hasAnyRole("ISLA")
                                .requestMatchers("/api/e-c/new-ec").hasAnyRole("ISLA")
                                .requestMatchers("/api/pc/").hasAnyRole("ISLA")
                                .requestMatchers("/api/pc/new-pc").hasAnyRole("ISLA", "PACIENTE")
                                .requestMatchers("/api/e-c/asignar").hasAnyRole("ISLA")
                                .requestMatchers("/api/enfermero/isla/**").hasAnyRole("ISLA")
                                .requestMatchers("/api/cama/disponibles/isla/**").hasAnyRole("ISLA")
                                .requestMatchers("/api/enfermero/camas/**").hasAnyRole("ENFERMERO")
                                .requestMatchers("/api/pc/find-pc/**").hasAnyRole("ISLA", "ENFERMERO")
                                .requestMatchers("/api/isla/detalle/**").hasAnyRole("ISLA")
                                .requestMatchers("/api/paciente/alta/**").hasAnyRole("ISLA")

                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(null))
                .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class)
                .logout(out -> out.logoutUrl("/api/auth/logout").clearAuthentication(true));

        return http.build();
    }
}