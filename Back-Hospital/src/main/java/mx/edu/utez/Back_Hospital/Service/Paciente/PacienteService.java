package mx.edu.utez.Back_Hospital.Service.Paciente;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteRepository;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_CamasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Pacientes_CamasRepository pacientes_camasRepository;

    @Autowired
    private CamaRepository camaRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAllPacientes(){
        List<PacienteBean> pacientes = pacienteRepository.findAll();
        return new ResponseEntity<>(new ApiResponse(pacientes, HttpStatus.OK, "obteniendo a todos los pacientes"), HttpStatus.OK);
    }

   @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> addPaciente(PacienteBean paciente){

        Optional<PacienteBean> optionalPaciente = pacienteRepository.findByCurp(paciente.getCurp());

        if(optionalPaciente.isPresent()){
            PacienteBean pacienteExistente = optionalPaciente.get();

            // 1. Si el paciente ya está activo en el hospital (alta = FALSE)
            if (!pacienteExistente.getAlta()) {
                return new ResponseEntity<>(
                        new ApiResponse(HttpStatus.CONFLICT,
                                "El paciente ya está registrado y actualmente activo en el hospital.", true),
                        HttpStatus.CONFLICT
                );
            }

            // 2. Si el paciente existe pero estaba dado de alta (alta = TRUE) → reactivarlo
            pacienteExistente.setAlta(false); // vuelve a estar activo

            // Aquí podrías agregar registro en el historial si lo implementas
            // historialService.registrarIngreso(pacienteExistente);

            pacienteRepository.saveAndFlush(pacienteExistente);

            return new ResponseEntity<>(
                    new ApiResponse(pacienteExistente, HttpStatus.OK, "Paciente reactivado correctamente."),
                    HttpStatus.OK
            );

        } else {

            // 3. Crear un nuevo paciente si no existe, y marcarlo como ACTIVO
            paciente.setAlta(false);

            // ENCRIPTAR LA CONTRASEÑA antes de guardar
            paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));

            PacienteBean nuevo = pacienteRepository.saveAndFlush(paciente);

            // Si manejas histórico:
            // historialService.registrarIngreso(nuevo);

            return new ResponseEntity<>(
                    new ApiResponse(nuevo, HttpStatus.CREATED, "Paciente registrado correctamente."),
                    HttpStatus.CREATED
            );
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> tokenEnabled(Long id, String token){
        Optional<PacienteBean> pacienteOptional =pacienteRepository.findById(id);

        if(pacienteOptional.isPresent()){
            PacienteBean paciente = pacienteOptional.get();
            paciente.setToken(token);

            pacienteRepository.save(paciente);

            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Token del dispositivo establecido", false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "usuario no encontrado", true), HttpStatus.NOT_FOUND);

    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> DarAlta(Long id){
        Optional<PacienteBean> pacienteOptional = pacienteRepository.findById(id);

        if (pacienteOptional.isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "Paciente no encontrado", true),
                    HttpStatus.NOT_FOUND
            );
        }

        PacienteBean paciente = pacienteOptional.get();
        paciente.setAlta(true);

        // 🔥 Buscar la relación Paciente-Cama por el ID del paciente
        Optional<Pacientes_Camas> optionalPC = pacientes_camasRepository.findByPacienteId(paciente.getId());
        if (optionalPC.isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "No se encontró relación Paciente-Cama", true),
                    HttpStatus.NOT_FOUND
            );
        }

        Pacientes_Camas pc = optionalPC.get();
        pc.setActivo(false);

        // 🔥 Obtener la cama asignada
        Optional<CamaBean> camaBeanOptional = camaRepository.findById(pc.getCama().getId());
        if (camaBeanOptional.isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "No se encontró la cama asignada", true),
                    HttpStatus.NOT_FOUND
            );
        }

        CamaBean cama = camaBeanOptional.get();
        cama.setOcupada(false);

        paciente.setToken("tokenDefault");

        pacienteRepository.save(paciente);
        pacientes_camasRepository.save(pc);
        camaRepository.save(cama);

        return new ResponseEntity<>(
                new ApiResponse(HttpStatus.OK, "Alta correctamente", false),
                HttpStatus.OK
        );
    }

}