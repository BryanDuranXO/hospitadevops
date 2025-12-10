package mx.edu.utez.Back_Hospital.Model.QR;

import lombok.Data;

@Data
public class DTOQRScan {
    private String qrContent;  // Contenido del QR escaneado (ej: "CAMA:5")
    private Long usuarioId;    // ID del usuario que escanea (puede ser paciente, enfermero o isla)
}