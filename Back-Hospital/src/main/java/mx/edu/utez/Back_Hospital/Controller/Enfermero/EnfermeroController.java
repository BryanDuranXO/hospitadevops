package mx.edu.utez.Back_Hospital.Controller.Enfermero;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Enfermero.DTO.DTOEnfermero;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroRepository;
import mx.edu.utez.Back_Hospital.Model.Isla.DTO.DtoPatchToken;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Rol.RolBean;
import mx.edu.utez.Back_Hospital.Model.Rol.RolRepository;
import mx.edu.utez.Back_Hospital.Service.Enfermero.EnfermeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enfermero")
@CrossOrigin(origins = {"*"})
public class EnfermeroController {

    @Autowired
    private EnfermeroService service;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getEnfermeros() {
        return service.getAll();
    }

    @GetMapping("/camas/{idEnfermero}")
    public ResponseEntity<ApiResponse> getCamas(@PathVariable Long idEnfermero) {
        return service.getCamasAsignadas(idEnfermero);
    }


    @PostMapping("/new-enfermero")
    public ResponseEntity<ApiResponse> saveEnfermero(@RequestBody DTOEnfermero dtoEnfermero) {
        RolBean rol = rolRepository.findById(dtoEnfermero.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        EnfermeroBean enfermero =dtoEnfermero.toEntity(rol);

        return service.saveEnfermero(enfermero);
    }

    @GetMapping("/isla/{id}")
    public ResponseEntity<ApiResponse> getEnfermerosByIsla(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiResponse(enfermeroRepository.findByIsla(id),
                        HttpStatus.OK,
                        "OK"), HttpStatus.OK);
    }

    @PatchMapping("/patch-token")
    public ResponseEntity<ApiResponse> updateToken(@RequestBody DtoPatchToken dto) {
        return service.tokenEnabled(dto.getId(), dto.getToken());
    }



}
