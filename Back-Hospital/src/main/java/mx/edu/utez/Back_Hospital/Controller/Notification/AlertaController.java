package mx.edu.utez.Back_Hospital.Controller.Notification;

import mx.edu.utez.Back_Hospital.Model.Notification.SolicitarAyudaRequest;
import mx.edu.utez.Back_Hospital.Service.Notification.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = {"*"})
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarAyuda(@RequestBody SolicitarAyudaRequest req) {
        try {
            alertaService.solicitarAyuda(req.getIdPaciente());
            return ResponseEntity.ok("Solicitud enviada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}