package mx.edu.utez.Back_Hospital.Service.Enfermeros_Camas;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroRepository;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.DTO.DTOEnfermeros_Camas;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_Camas;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_CamasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Enfermeros_CamasService {
    @Autowired
    private Enfermeros_CamasRepository repository;

    @Autowired
    private CamaRepository camaRepository;

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAll(){
        List<Enfermeros_Camas> enfermerosCamasList = repository.findAll();
        return new ResponseEntity<>(new ApiResponse(enfermerosCamasList, HttpStatus.OK, "obteniendo enfermeros y sus camas asignadas"), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> save(Enfermeros_Camas enfermeros_Camas){

        EnfermeroBean enf = enfermeroRepository.findById(3L).orElse(null);
        System.out.println(enf);

        Optional<CamaBean> camaOptional = camaRepository.findById(enfermeros_Camas.getCama().getId());
        Optional<EnfermeroBean> enfermeroOptional = enfermeroRepository.findById(enfermeros_Camas.getEnfermero().getId());

        if(!enfermeroOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Enfermero no encontrado", true), HttpStatus.CONFLICT);
        }

        if(!camaOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Cama no encontrada", true), HttpStatus.CONFLICT);
        }

        CamaBean cama = camaOptional.get();
        EnfermeroBean enfermero = enfermeroOptional.get();

        enfermeros_Camas.setCama(cama);
        enfermeros_Camas.setEnfermero(enfermero);

        return new ResponseEntity<>(new ApiResponse(repository.saveAndFlush(enfermeros_Camas), HttpStatus.CREATED, "Enfermero asignado"), HttpStatus.CREATED);

    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> asignarPorIsla(DTOEnfermeros_Camas dto) {

        Long idIsla = dto.getIdIsla();
        if (idIsla == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, "Falta idIsla", true), HttpStatus.BAD_REQUEST);
        }

        // 1️⃣ Buscar enfermeros de esa isla
        List<EnfermeroBean> enfermeros = enfermeroRepository.findByIsla(idIsla);
        if (enfermeros.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No hay enfermeros en esta isla", true),
                    HttpStatus.NOT_FOUND);
        }

        // 2️⃣ Buscar camas disponibles de esa misma isla
        List<CamaBean> camasDisponibles = camaRepository.findCamasDisponiblesByIsla(idIsla);
        if (camasDisponibles.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "No hay camas disponibles en la isla", true),
                    HttpStatus.NOT_FOUND);
        }

        // 3️⃣ Ordenar enfermeros por número de camas asignadas (para reparto equitativo)
        enfermeros.sort((a, b) -> {
            int countA = repository.countCamasAsignadas(a.getId());
            int countB = repository.countCamasAsignadas(b.getId());
            return Integer.compare(countA, countB);
        });

        // 4️⃣ Tomar la cama que llega en el DTO
        CamaBean cama = camaRepository.findById(dto.getCama()).orElse(null);
        if (cama == null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Cama no encontrada", true),
                    HttpStatus.CONFLICT);
        }

        // 5️⃣ Asignar al enfermero con MENOS camas
        EnfermeroBean enfermeroAsignado = enfermeros.get(0);

        Enfermeros_Camas nuevaAsignacion = new Enfermeros_Camas();
        nuevaAsignacion.setCama(cama);
        nuevaAsignacion.setEnfermero(enfermeroAsignado);
        nuevaAsignacion.setActivo(true);

        repository.saveAndFlush(nuevaAsignacion);

        return new ResponseEntity<>(new ApiResponse(
                nuevaAsignacion,
                HttpStatus.CREATED,
                "Cama asignada correctamente al enfermero de la isla"
        ), HttpStatus.CREATED);
    }

}
