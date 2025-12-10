package mx.edu.utez.Back_Hospital.Controller.Enfermeros_Camas;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroRepository;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.DTO.DTOEnfermeros_Camas;
import mx.edu.utez.Back_Hospital.Service.Enfermeros_Camas.Enfermeros_CamasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/e-c")
@CrossOrigin(origins = {"*"})
public class Enfermeros_CamasController {

    @Autowired
    private Enfermeros_CamasService service;

    @Autowired
    private CamaRepository camaRepository;

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> findAll(){
        return service.getAll();
    }

    @PostMapping("/new-ec")
    public ResponseEntity<ApiResponse> save(@RequestBody DTOEnfermeros_Camas dto){

        CamaBean cama = camaRepository.findById(dto.getCama())
                .orElse(null);
        EnfermeroBean enfermero = enfermeroRepository.findById(dto.getEnfermero())
                .orElse(null);

        if (enfermero == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Enfermero no encontrado", true), HttpStatus.CONFLICT);
        }

        if (cama == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Cama no encontrada", true), HttpStatus.CONFLICT);
        }



        return new ResponseEntity<>(new ApiResponse(
                service.save(dto.toEntity()),
                HttpStatus.CREATED,
                "Asignación creada")
                , HttpStatus.CREATED);
    }

    @PostMapping("/asignar")
    public ResponseEntity<ApiResponse> asignarPorIsla(@RequestBody DTOEnfermeros_Camas dto) {
        return service.asignarPorIsla(dto);
    }


}
