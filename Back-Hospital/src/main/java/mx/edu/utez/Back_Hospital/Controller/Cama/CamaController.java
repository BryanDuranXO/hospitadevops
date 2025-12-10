package mx.edu.utez.Back_Hospital.Controller.Cama;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Cama.DTO.DTOCama;
import mx.edu.utez.Back_Hospital.Service.Camas.CamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cama")
@CrossOrigin(origins = {"*"})
public class CamaController {
    @Autowired
    private CamaService service;

    @Autowired
    private CamaRepository camaRepository;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getCamas(){
        return service.getAll();
    }

    @PostMapping("/new-cama")
    public ResponseEntity<ApiResponse> saveCama(@RequestBody CamaBean camaBean){
        return service.save(camaBean);
    }

    @GetMapping("/disponibles/isla/{id}")
    public ResponseEntity<ApiResponse> getCamasDisponiblesByIsla(@PathVariable Long id) {
        return new ResponseEntity<>(
                new ApiResponse(camaRepository.findCamasDisponiblesByIsla(id),
                        HttpStatus.OK,
                        "OK"), HttpStatus.OK);
    }



}
