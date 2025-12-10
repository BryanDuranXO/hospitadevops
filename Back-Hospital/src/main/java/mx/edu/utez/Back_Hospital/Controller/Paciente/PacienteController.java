package mx.edu.utez.Back_Hospital.Controller.Paciente;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Isla.DTO.DtoPatchToken;
import mx.edu.utez.Back_Hospital.Model.Paciente.DTO.DTOPaciente;
import mx.edu.utez.Back_Hospital.Model.Paciente.DTO.DTOPaciente_PatchToken;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteRepository;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;
import mx.edu.utez.Back_Hospital.Model.Rol.RolRepository;
import mx.edu.utez.Back_Hospital.Service.Paciente.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paciente")
@CrossOrigin(origins = {"*"})
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll() {
        return pacienteService.getAllPacientes();
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(
            @Validated(DTOPaciente.Register.class) @RequestBody DTOPaciente dtoPaciente) {

        RolBean rol = rolRepository.findById(dtoPaciente.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        PacienteBean paciente = dtoPaciente.toEntity(rol);

        // Llamar al servicio
        return pacienteService.addPaciente(paciente);
    }

    @PatchMapping("/patch-token")
    public ResponseEntity<ApiResponse> updateToken(@RequestBody DTOPaciente_PatchToken dto) {
        return pacienteService.tokenEnabled(dto.getId(), dto.getToken());
    }

    @PatchMapping("/alta/{id}")
    public ResponseEntity<ApiResponse> AltaPaciente(@PathVariable Long id){
        return pacienteService.DarAlta(id);
    }

}
