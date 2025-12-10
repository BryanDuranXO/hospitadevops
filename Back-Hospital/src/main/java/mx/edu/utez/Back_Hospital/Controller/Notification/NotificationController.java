package mx.edu.utez.Back_Hospital.Controller.Notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import mx.edu.utez.Back_Hospital.Model.Notification.NotificationRequest;
import mx.edu.utez.Back_Hospital.Service.Notification.FirebaseMessagingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = {"*"})
public class NotificationController {

    private final FirebaseMessagingService firebaseService;

    public NotificationController(FirebaseMessagingService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/send")
    public String send(@RequestBody NotificationRequest request)
            throws FirebaseMessagingException {
        return firebaseService.sendNotification(request);
    }
}
