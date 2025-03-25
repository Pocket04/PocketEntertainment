package app.notification.service;

import app.notification.dto.NotificationRequest;
import app.notification.repository.NotificationFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationFeignClient notificationFeignClient;


    @Autowired
    public NotificationService(NotificationFeignClient notificationFeignClient) {
        this.notificationFeignClient = notificationFeignClient;
    }

    public void sendNotification(NotificationRequest notificationRequest, UUID id) {

        notificationRequest.setUserId(id);
        notificationFeignClient.sendNotification(notificationRequest);

    }



}
