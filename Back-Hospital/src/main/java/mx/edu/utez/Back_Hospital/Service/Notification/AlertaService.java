package mx.edu.utez.Back_Hospital.Service.Notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_Camas;
import mx.edu.utez.Back_Hospital.Model.Enfermeros_Camas.Enfermeros_CamasRepository;
import mx.edu.utez.Back_Hospital.Model.Isla.IslaBean;
import mx.edu.utez.Back_Hospital.Model.Notification.NotificationRequest;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_CamasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AlertaService {

    private final Pacientes_CamasRepository pacientesCamasRepo;
    private final Enfermeros_CamasRepository enfermerosCamasRepo;
    private final FirebaseMessagingService firebaseService;

    public AlertaService(
            Pacientes_CamasRepository pacientesCamasRepo,
            Enfermeros_CamasRepository enfermerosCamasRepo,
            FirebaseMessagingService firebaseService
    ) {
        this.pacientesCamasRepo = pacientesCamasRepo;
        this.enfermerosCamasRepo = enfermerosCamasRepo;
        this.firebaseService = firebaseService;
    }

    @Transactional
    public void solicitarAyuda(Long idPaciente) throws FirebaseMessagingException {

        // 1. Obtener la cama actual del paciente
        Pacientes_Camas relacion = pacientesCamasRepo
                .findByPacienteIdAndActivoTrue(idPaciente)
                .orElseThrow(() -> new RuntimeException("El paciente no tiene cama asignada"));

        CamaBean cama = relacion.getCama();

        // 2. Obtener enfermero asignado
        Enfermeros_Camas asignacion = enfermerosCamasRepo
                .findByCamaIdAndActivoTrue(cama.getId())
                .orElseThrow(() -> new RuntimeException("La cama no tiene enfermero asignado"));

        EnfermeroBean enfermero = asignacion.getEnfermero();

        // 3. Obtener isla
        IslaBean isla = cama.getIslaBean();

        // 🔥 4. Crear notificación
        NotificationRequest reqEnfermero = new NotificationRequest();
        reqEnfermero.setToken(enfermero.getToken());
        reqEnfermero.setTitle("Paciente solicita ayuda");
        reqEnfermero.setBody("Un paciente asignado en cama: " + cama.getCama() +" requiere asistencia");
        reqEnfermero.setData(Map.of(
                "pacienteId", idPaciente.toString(),
                "camaId", cama.getId().toString()
        ));

        NotificationRequest reqIsla = new NotificationRequest();
        reqIsla.setToken(isla.getToken());
        reqIsla.setTitle("Alerta de paciente");
        reqIsla.setBody("Un paciente en cama: " + cama.getCama()+ " de esta isla solicita ayuda");
        reqIsla.setData(Map.of(
                "pacienteId", idPaciente.toString(),
                "camaId", cama.getId().toString()
        ));

        // 🔥 5. Enviar
        firebaseService.sendNotification(reqEnfermero);
        firebaseService.sendNotification(reqIsla);
    }
}