package mx.edu.utez.Back_Hospital.Service.Camas;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Cama.DTO.DTOCama;
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
public class CamaService {

    @Autowired
    private CamaRepository camaRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAll() {
        List<DTOCama> camas = camaRepository.findAll()
                .stream()
                .map(DTOCama::new)   // → convierte cada CamaBean en DTOCama
                .toList();

        ApiResponse response = new ApiResponse(
                camas,
                HttpStatus.OK,
                "Obteniendo todas las camas"
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> save(CamaBean camaBean){
        Optional<CamaBean> camaOptional = camaRepository.findByCama(camaBean.getCama());

        if(camaOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "La información ya se encuentra registrada", true), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(new ApiResponse(camaRepository.saveAndFlush(camaBean), HttpStatus.CREATED, "Cama creada correctamente"), HttpStatus.CREATED);
    }
}
