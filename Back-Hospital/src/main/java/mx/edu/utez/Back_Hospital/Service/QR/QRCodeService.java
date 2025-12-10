package mx.edu.utez.Back_Hospital.Service.QR;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodeService {

    /**
     * Genera un código QR en formato Base64
     *
     * @param data   Información a codificar (normalmente el ID de la cama)
     * @param width  Ancho del QR en píxeles
     * @param height Alto del QR en píxeles
     * @return String en Base64 del código QR
     */
    public String generateQRCodeBase64(String data, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        byte[] qrBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(qrBytes);
    }

    /**
     * Genera el contenido del QR para una cama
     * Formato: CAMA:{id_cama}
     */
    public String generateCamaQRContent(Long camaId) {
        return "CAMA:" + camaId;
    }

    /**
     * Extrae el ID de la cama desde el contenido del QR
     */
    public Long extractCamaIdFromQR(String qrContent) {
        if (qrContent != null && qrContent.startsWith("CAMA:")) {
            try {
                return Long.parseLong(qrContent.substring(5));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}