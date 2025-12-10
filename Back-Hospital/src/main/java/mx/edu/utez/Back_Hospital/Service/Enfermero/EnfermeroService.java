package mx.edu.utez.Back_Hospital.Service.Enfermero;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.DTO.DTOCamaSimple;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroRepository;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_CamasRepository;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
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
public class EnfermeroService {

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Enfermeros_CamasRepository enfermerosCamasRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAll(){
        List<EnfermeroBean> enfermeros = enfermeroRepository.findAll();

        return new ResponseEntity<>(new ApiResponse(enfermeros, HttpStatus.OK, "Mostrando todos los enfermeros"), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getCamasAsignadas(Long idEnfermero) {
        List<CamaBean> camas = enfermerosCamasRepository.findCamasByEnfermero(idEnfermero);

        List<DTOCamaSimple> camasDTO = camas.stream()
                .map(DTOCamaSimple::new)
                .toList();

        return new ResponseEntity<>(
                new ApiResponse(camasDTO, HttpStatus.OK, "Camas asignadas al enfermero"),
                HttpStatus.OK
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> saveEnfermero(EnfermeroBean enfermeroBean){
        Optional<EnfermeroBean> enfermeroOptional = enfermeroRepository.findByTelefono(enfermeroBean.getTelefono());

        if(enfermeroOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "La información ya se encuentra registrada", true), HttpStatus.CONFLICT);
        }

        enfermeroBean.setPassword(passwordEncoder.encode(enfermeroBean.getPassword()));

        return new ResponseEntity<>(new ApiResponse(enfermeroRepository.saveAndFlush(enfermeroBean), HttpStatus.CREATED, "Enfermero registrado correctamente"), HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> tokenEnabled(Long id, String token){
        Optional<EnfermeroBean> enfermeroOptional = enfermeroRepository.findById(id);

        if(enfermeroOptional.isPresent()){
            EnfermeroBean enfermero = enfermeroOptional.get();
            enfermero.setToken(token);

            enfermeroRepository.save(enfermero);

            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Token del dispositivo establecido", false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "usuario no encontrado", true), HttpStatus.NOT_FOUND);

    }
}
