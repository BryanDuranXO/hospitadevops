package mx.edu.utez.Back_Hospital.Service.Isla;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.DTO.CamaDetalle;
import mx.edu.utez.Back_Hospital.Model.Enfermero.DTO.EnfermeroDetalle;
import mx.edu.utez.Back_Hospital.Model.Isla.DTO.DetalleIslaDTO;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaRepository;
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
public class IslaService {

    @Autowired
    private IslaRepository islaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAll(){
        List<IslaBean> islaBeans = islaRepository.findAll();
        return new ResponseEntity<>(new ApiResponse(islaBeans, HttpStatus.OK, "Obteniendo todas las islas"),HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> save(IslaBean islaBean){
        Optional<IslaBean> islaOptional = islaRepository.findByNumero(islaBean.getNumero());

        if(islaOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.CONFLICT, "Isla ya registrada", true), HttpStatus.CONFLICT);
        }

        islaBean.setPassword(passwordEncoder.encode(islaBean.getPassword()));
        return new ResponseEntity<>(new ApiResponse(islaRepository.saveAndFlush(islaBean), HttpStatus.CREATED, "Isla creada exitosamente"), HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> tokenEnabled(Long id, String token){
        Optional<IslaBean> islaOptional = islaRepository.findById(id);

        if(islaOptional.isPresent()){
         IslaBean isla = islaOptional.get();
         isla.setToken(token);

         islaRepository.save(isla);

         return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Token del dispositivo establecido", false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "usuario no encontrado", true), HttpStatus.NOT_FOUND);

    }


    public DetalleIslaDTO buildDetalle(IslaBean isla) {
        List<EnfermeroDetalle> enfermeros = isla.getEnfermeroBeanSet()
                .stream()
                .map(EnfermeroDetalle::new)
                .toList();

        List<CamaDetalle> camas = isla.getCamaBeans()
                .stream()
                .map(CamaDetalle::new)
                .toList();

        return new DetalleIslaDTO(
                isla.getId(),
                isla.getNumero(),
                isla.getToken(),
                enfermeros,
                camas
        );
    }



}
