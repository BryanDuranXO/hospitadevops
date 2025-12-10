package mx.edu.utez.Back_Hospital.Controller.Pacientes_Camas;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.DTO.DTOPacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_CamasRepository;
import mx.edu.utez.Back_Hospital.Service.Pacientes_Camas.Pacientes_CamasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pc")
@CrossOrigin(origins = {"*"})
public class Pacientes_CamasController {
    @Autowired
    private Pacientes_CamasService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getPacientes_Camas() {
        return service.getAll();
    }

    @PostMapping("/new-pc")
    public ResponseEntity<ApiResponse> save(@RequestBody DTOPacientes_Camas dto){
        return service.save(dto.toEntity());
    }

    @GetMapping("/find-pc/{id}")
    public ResponseEntity<ApiResponse> getPacientes_Camas(@PathVariable Long id){
        return service.getById(id);
    }
}
