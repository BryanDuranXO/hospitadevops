package mx.edu.utez.Back_Hospital.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

   /* @PostConstruct
    public void initialize() throws IOException {

        InputStream serviceAccount = null;

        // 1️⃣ Intentar cargar desde resources (modo LOCAL)
        serviceAccount = getClass().getClassLoader()
                .getResourceAsStream("firebase/serviceAccountKey.json");

        // 2️⃣ Si no existe (modo DOCKER), cargar desde /app/firebase/
        if (serviceAccount == null) {
            File dockerPath = new File("/app/firebase/serviceAccountKey.json");
            if (dockerPath.exists()) {
                serviceAccount = new FileInputStream(dockerPath);
            }
        }

        // 3️⃣ Si aún así no existe → error real
        if (serviceAccount == null) {
            throw new RuntimeException("No se encontró serviceAccountKey.json ni en resources ni en /app/firebase/");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase inicializado correctamente.");
        }
    }*/
}
