package mx.edu.utez.Back_Hospital.Model.Rol;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsertRoles {
    @Autowired
    private RolRepository  rolRepository;

    @PostConstruct
    public void init(){
        if(!rolRepository.existsByRol("ISLA")){
            rolRepository.save(new RolBean("ISLA"));
        }

        if(!rolRepository.existsByRol("ENFERMERO")){
            rolRepository.save(new RolBean("ENFERMERO"));
        }

        if(!rolRepository.existsByRol("PACIENTE")){
            rolRepository.save(new RolBean("PACIENTE"));
        }
    }
}
