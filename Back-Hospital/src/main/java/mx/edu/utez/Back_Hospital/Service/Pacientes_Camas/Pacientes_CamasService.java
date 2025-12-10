package mx.edu.utez.Back_Hospital.Service.Pacientes_Camas;

import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteRepository;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_CamasRepository;
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
public class Pacientes_CamasService {

    @Autowired
    private Pacientes_CamasRepository repository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CamaRepository camaRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getAll(){
        List<Pacientes_Camas> pc = repository.findAll();

        return new ResponseEntity<>(new ApiResponse(pc, HttpStatus.OK, "Obteniendo a todos los pacientes y sus camas"), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> save(Pacientes_Camas pacientesCamas){
        Optional<PacienteBean> pacienteOptional = pacienteRepository.findById(pacientesCamas.getPaciente().getId());
        Optional<CamaBean> camaBeanOptional = camaRepository.findById(pacientesCamas.getCama().getId());

        if(!pacienteOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "paciente no encontrado", true), HttpStatus.NOT_FOUND);
        }

        if(!camaBeanOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "cama no encontrada", true), HttpStatus.NOT_FOUND);
        }
        CamaBean camaBean = camaBeanOptional.get();
        camaBean.setOcupada(true);
        camaRepository.save(camaBean);
        return new ResponseEntity<>(new ApiResponse(repository.saveAndFlush(pacientesCamas), HttpStatus.CREATED, "paciente asignado a cama correctamente"), HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> getById(Long id){
        Optional<Pacientes_Camas> pacienteOptional = repository.findByCama_Id(id);

        if(pacienteOptional.isPresent()){
            return new ResponseEntity<>(new ApiResponse(pacienteOptional, HttpStatus.OK,"Obteniendo información de la cama"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "cama no encontrada", true), HttpStatus.NOT_FOUND);
    }


}
