package mx.edu.utez.Back_Hospital.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiResponse {
    private Object data;
    private HttpStatus status;
    private String message;
    private boolean error;

    public ApiResponse(Object data, HttpStatus status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(HttpStatus status, String message, boolean error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }
}
