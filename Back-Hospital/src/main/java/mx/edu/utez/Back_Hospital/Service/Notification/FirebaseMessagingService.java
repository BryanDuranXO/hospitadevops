package mx.edu.utez.Back_Hospital.Service.Notification;

import com.google.firebase.messaging.*;
import mx.edu.utez.Back_Hospital.Model.Notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    public String sendNotification(NotificationRequest request) throws FirebaseMessagingException {

        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        Message.Builder builder = Message.builder().setNotification(notification);

        if (request.getData() != null) {
            builder.putAllData(request.getData());
        }

        if (request.getToken() != null && !request.getToken().isEmpty()) {
            builder.setToken(request.getToken());
        } else if (request.getTopic() != null && !request.getTopic().isEmpty()) {
            builder.setTopic(request.getTopic());
        } else {
            throw new IllegalArgumentException("Debes enviar 'token' o 'topic'");
        }

        return FirebaseMessaging.getInstance().send(builder.build());
    }
}