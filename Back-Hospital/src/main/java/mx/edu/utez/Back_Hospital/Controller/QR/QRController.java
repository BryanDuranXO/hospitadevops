package mx.edu.utez.Back_Hospital.Controller.QR;

import com.google.zxing.WriterException;
import mx.edu.utez.Back_Hospital.Config.ApiResponse;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaBean;
import mx.edu.utez.Back_Hospital.Model.Cama.CamaRepository;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroBean;
import mx.edu.utez.Back_Hospital.Model.Enfermero.EnfermeroRepository;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteBean;
import mx.edu.utez.Back_Hospital.Model.Paciente.PacienteRepository;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_Camas;
import mx.edu.utez.Back_Hospital.Model.Pacientes_Camas.Pacientes_CamasRepository;
import mx.edu.utez.Back_Hospital.Model.QR.DTOQRScan;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioBean;
import mx.edu.utez.Back_Hospital.Model.Usuarios.UsuarioRepository;
import mx.edu.utez.Back_Hospital.Service.QR.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin(origins = {"*"})
public class QRController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private CamaRepository camaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EnfermeroRepository enfermeroRepository;

    @Autowired
    private Pacientes_CamasRepository pacientesCamasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Genera el código QR para una cama específica
     * GET /api/qr/generate/{camaId}
     */
    @GetMapping("/generate/{camaId}")
    public ResponseEntity<ApiResponse> generateQRForCama(@PathVariable Long camaId) {
        try {
            // Verificar que la cama existe
            CamaBean cama = camaRepository.findById(camaId).orElse(null);
            if (cama == null) {
                return new ResponseEntity<>(
                        new ApiResponse(HttpStatus.NOT_FOUND, "Cama no encontrada", true),
                        HttpStatus.NOT_FOUND
                );
            }

            // Generar contenido del QR
            String qrContent = qrCodeService.generateCamaQRContent(camaId);

            // Generar imagen QR en Base64
            String qrBase64 = qrCodeService.generateQRCodeBase64(qrContent, 300, 300);

            // Preparar respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("camaId", camaId);
            response.put("qrContent", qrContent);
            response.put("qrImage", "data:image/png;base64," + qrBase64);
            response.put("numeroCama", cama.getCama());
            response.put("ocupada", cama.isOcupada());

            // Información de la isla
            if (cama.getIslaBean() != null) {
                Map<String, Object> islaInfo = new HashMap<>();
                islaInfo.put("id", cama.getIslaBean().getId());
                islaInfo.put("numero", cama.getIslaBean().getNumero());
                islaInfo.put("nombre", cama.getIslaBean().getNombre());
                response.put("isla", islaInfo);
            }

            return new ResponseEntity<>(
                    new ApiResponse(response, HttpStatus.OK, "QR generado exitosamente"),
                    HttpStatus.OK
            );

        } catch (WriterException | IOException e) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error al generar QR: " + e.getMessage(), true),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/image/{camaId}")
    public ResponseEntity<byte[]> generateQRImage(@PathVariable Long camaId) {
        try {
            CamaBean cama = camaRepository.findById(camaId).orElse(null);
            if (cama == null) {
                return ResponseEntity.notFound().build();
            }

            String qrContent = qrCodeService.generateCamaQRContent(camaId);
            String qrBase64 = qrCodeService.generateQRCodeBase64(qrContent, 300, 300);

            // Convertir Base64 a bytes
            byte[] imageBytes = Base64.getDecoder().decode(qrBase64);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageBytes.length);
            // Opcional: hacer que se descargue automáticamente
            // headers.setContentDispositionFormData("attachment", "cama_" + camaId + "_qr.png");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * Procesa el escaneo de un QR
     * POST /api/qr/scan
     * Body: { "qrContent": "CAMA:5", "usuarioId": 10 }
     */
    @PostMapping("/scan")
    @Transactional
    public ResponseEntity<ApiResponse> scanQR(@RequestBody DTOQRScan dto) {

        // Validaciones básicas
        if (dto.getQrContent() == null || dto.getUsuarioId() == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Faltan datos: qrContent y usuarioId son requeridos", true),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Extraer ID de la cama del QR
        Long camaId = qrCodeService.extractCamaIdFromQR(dto.getQrContent());
        if (camaId == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST, "QR inválido", true),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Verificar que la cama existe
        CamaBean cama = camaRepository.findById(camaId).orElse(null);
        if (cama == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "Cama no encontrada", true),
                    HttpStatus.NOT_FOUND
            );
        }

        // Buscar el usuario y obtener su rol
        UsuarioBean usuario = usuarioRepository.findById(dto.getUsuarioId()).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado", true),
                    HttpStatus.NOT_FOUND
            );
        }

        // Verificar que el usuario esté activo
        if (!usuario.isStatus()) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.FORBIDDEN, "Usuario inactivo", true),
                    HttpStatus.FORBIDDEN
            );
        }

        // Obtener el rol del usuario
        if (usuario.getRol() == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                            "El usuario no tiene un rol asignado", true),
                    HttpStatus.BAD_REQUEST
            );
        }

        String nombreRol = usuario.getRol().getRol().toUpperCase();

        // Lógica según el rol del usuario
        switch (nombreRol) {
            case "PACIENTE":
                return processPacienteScan(cama, dto.getUsuarioId());

            case "ENFERMERO":
                return processEnfermeroScan(cama, dto.getUsuarioId());

            case "ISLA":
                // Las islas pueden ver información similar a los enfermeros
                return processIslaScan(cama, dto.getUsuarioId());

            default:
                return new ResponseEntity<>(
                        new ApiResponse(HttpStatus.FORBIDDEN,
                                "El rol '" + nombreRol + "' no tiene permisos para escanear QR de camas", true),
                        HttpStatus.FORBIDDEN
                );
        }
    }

    /**
     * Procesa el escaneo cuando es un PACIENTE
     * Asigna el paciente a la cama
     */
    private ResponseEntity<ApiResponse> processPacienteScan(CamaBean cama, Long pacienteId) {
        // Verificar que el paciente existe
        PacienteBean paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "Paciente no encontrado", true),
                    HttpStatus.NOT_FOUND
            );
        }

        // Verificar si el paciente está activo
        if (!paciente.isStatus()) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.CONFLICT, "El paciente no está activo en el sistema", true),
                    HttpStatus.CONFLICT
            );
        }

        // Verificar si la cama ya está ocupada (buscando asignaciones activas)
        List<Pacientes_Camas> asignacionesActivas = pacientesCamasRepository
                .findByCamaAndActivoTrue(cama);

        if (!asignacionesActivas.isEmpty()) {
            Pacientes_Camas asignacionActual = asignacionesActivas.get(0);
            PacienteBean pacienteActual = asignacionActual.getPaciente();

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Esta cama ya está ocupada");
            response.put("camaId", cama.getId());
            response.put("numeroCama", cama.getCama());
            response.put("pacienteActual", Map.of(
                    "id", pacienteActual.getId(),
                    "nombre", pacienteActual.getNombre() + " " +
                            pacienteActual.getPaterno() + " " +
                            pacienteActual.getMaterno()
            ));

            return new ResponseEntity<>(
                    new ApiResponse(response, HttpStatus.CONFLICT, "Cama ocupada"),
                    HttpStatus.CONFLICT
            );
        }

        // Verificar si el paciente ya tiene una cama asignada activa
        List<Pacientes_Camas> asignacionesPaciente = pacientesCamasRepository
                .findByPacienteAndActivoTrue(paciente);

        if (!asignacionesPaciente.isEmpty()) {
            Pacientes_Camas asignacionActual = asignacionesPaciente.get(0);
            CamaBean camaActual = asignacionActual.getCama();

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Este paciente ya tiene una cama asignada");
            response.put("pacienteId", paciente.getId());
            response.put("camaActual", Map.of(
                    "id", camaActual.getId(),
                    "numero", camaActual.getCama()
            ));

            return new ResponseEntity<>(
                    new ApiResponse(response, HttpStatus.CONFLICT, "Paciente ya tiene cama asignada"),
                    HttpStatus.CONFLICT
            );
        }

        // Crear la asignación del paciente a la cama
        Pacientes_Camas nuevaAsignacion = new Pacientes_Camas();
        nuevaAsignacion.setPaciente(paciente);
        nuevaAsignacion.setCama(cama);
        nuevaAsignacion.setActivo(true);

        pacientesCamasRepository.save(nuevaAsignacion);

        // Actualizar el estado de la cama
        cama.setOcupada(true);
        camaRepository.save(cama);

        // Preparar respuesta exitosa
        Map<String, Object> response = new HashMap<>();
        response.put("accion", "ASIGNACION_EXITOSA");
        response.put("asignacionId", nuevaAsignacion.getId());
        response.put("camaId", cama.getId());
        response.put("numeroCama", cama.getCama());
        response.put("paciente", Map.of(
                "id", paciente.getId(),
                "nombre", paciente.getNombre() + " " +
                        paciente.getPaterno() + " " +
                        paciente.getMaterno(),
                "curp", paciente.getCurp()
        ));

        return new ResponseEntity<>(
                new ApiResponse(response, HttpStatus.CREATED, "Paciente asignado exitosamente a la cama"),
                HttpStatus.CREATED
        );
    }

    /**
     * Procesa el escaneo cuando es un ENFERMERO
     * Muestra información de la cama
     */
    private ResponseEntity<ApiResponse> processEnfermeroScan(CamaBean cama, Long enfermeroId) {
        // Verificar que el enfermero existe
        EnfermeroBean enfermero = enfermeroRepository.findById(enfermeroId).orElse(null);
        if (enfermero == null) {
            return new ResponseEntity<>(
                    new ApiResponse(HttpStatus.NOT_FOUND, "Enfermero no encontrado", true),
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, Object> response = new HashMap<>();
        response.put("accion", "VER_INFORMACION");
        response.put("tipoUsuario", "ENFERMERO");
        response.put("camaId", cama.getId());
        response.put("numeroCama", cama.getCama());
        response.put("ocupada", cama.isOcupada());

        // Información de la isla
        if (cama.getIslaBean() != null) {
            response.put("isla", Map.of(
                    "id", cama.getIslaBean().getId(),
                    "numero", cama.getIslaBean().getNumero(),
                    "nombre", cama.getIslaBean().getNombre()
            ));
        }

        // Información del paciente actual si existe (buscar asignación activa)
        List<Pacientes_Camas> asignacionesActivas = pacientesCamasRepository
                .findByCamaAndActivoTrue(cama);

        if (!asignacionesActivas.isEmpty()) {
            Pacientes_Camas asignacionActual = asignacionesActivas.get(0);
            PacienteBean paciente = asignacionActual.getPaciente();

            Map<String, Object> pacienteInfo = new HashMap<>();
            pacienteInfo.put("id", paciente.getId());
            pacienteInfo.put("nombre", paciente.getNombre());
            pacienteInfo.put("paterno", paciente.getPaterno());
            pacienteInfo.put("materno", paciente.getMaterno());
            pacienteInfo.put("nombreCompleto",
                    paciente.getNombre() + " " +
                            paciente.getPaterno() + " " +
                            paciente.getMaterno());
            pacienteInfo.put("curp", paciente.getCurp());
            pacienteInfo.put("telefono", paciente.getTelefono());
            pacienteInfo.put("padecimientos", paciente.getPadecimientos());
            pacienteInfo.put("alta", paciente.getAlta());

            response.put("paciente", pacienteInfo);
        } else {
            response.put("paciente", null);
            response.put("mensaje", "No hay paciente asignado a esta cama");
        }

        // Información del enfermero que consulta
        response.put("enfermeroConsulta", Map.of(
                "id", enfermero.getId(),
                "nombre", enfermero.getNombre() + " " +
                        enfermero.getPaterno() + " " +
                        enfermero.getMaterno(),
                "isla", enfermero.getIsla() != null ? Map.of(
                        "id", enfermero.getIsla().getId(),
                        "numero", enfermero.getIsla().getNumero()
                ) : null
        ));

        return new ResponseEntity<>(
                new ApiResponse(response, HttpStatus.OK, "Información de la cama"),
                HttpStatus.OK
        );
    }

    /**
     * Procesa el escaneo cuando es una ISLA
     * Muestra información completa de la cama (similar a enfermero pero con más detalles)
     */
    private ResponseEntity<ApiResponse> processIslaScan(CamaBean cama, Long islaId) {
        Map<String, Object> response = new HashMap<>();
        response.put("accion", "VER_INFORMACION");
        response.put("tipoUsuario", "ISLA");
        response.put("camaId", cama.getId());
        response.put("numeroCama", cama.getCama());
        response.put("ocupada", cama.isOcupada());

        // Información de la isla
        if (cama.getIslaBean() != null) {
            response.put("isla", Map.of(
                    "id", cama.getIslaBean().getId(),
                    "numero", cama.getIslaBean().getNumero(),
                    "nombre", cama.getIslaBean().getNombre()
            ));
        }

        // Información del paciente actual si existe
        List<Pacientes_Camas> asignacionesActivas = pacientesCamasRepository
                .findByCamaAndActivoTrue(cama);

        if (!asignacionesActivas.isEmpty()) {
            Pacientes_Camas asignacionActual = asignacionesActivas.get(0);
            PacienteBean paciente = asignacionActual.getPaciente();

            Map<String, Object> pacienteInfo = new HashMap<>();
            pacienteInfo.put("id", paciente.getId());
            pacienteInfo.put("nombre", paciente.getNombre());
            pacienteInfo.put("paterno", paciente.getPaterno());
            pacienteInfo.put("materno", paciente.getMaterno());
            pacienteInfo.put("nombreCompleto",
                    paciente.getNombre() + " " +
                            paciente.getPaterno() + " " +
                            paciente.getMaterno());
            pacienteInfo.put("curp", paciente.getCurp());
            pacienteInfo.put("telefono", paciente.getTelefono());
            pacienteInfo.put("padecimientos", paciente.getPadecimientos());
            pacienteInfo.put("alta", paciente.getAlta());
            pacienteInfo.put("usuario", paciente.getUsuario());

            response.put("paciente", pacienteInfo);
        } else {
            response.put("paciente", null);
            response.put("mensaje", "No hay paciente asignado a esta cama");
        }

        // Información de enfermeros asignados a esta cama
        if (cama.getAsignacionesEnfermeros() != null && !cama.getAsignacionesEnfermeros().isEmpty()) {
            List<Map<String, Object>> enfermerosAsignados = cama.getAsignacionesEnfermeros().stream()
                    .filter(asig -> asig.isActivo())
                    .map(asig -> {
                        EnfermeroBean enf = asig.getEnfermero();
                        Map<String, Object> enfInfo = new HashMap<>();
                        enfInfo.put("id", enf.getId());
                        enfInfo.put("nombre", enf.getNombre() + " " +
                                enf.getPaterno() + " " +
                                enf.getMaterno());
                        return enfInfo;
                    })
                    .toList();
            response.put("enfermerosAsignados", enfermerosAsignados);
        }

        return new ResponseEntity<>(
                new ApiResponse(response, HttpStatus.OK, "Información completa de la cama"),
                HttpStatus.OK
        );
    }
}