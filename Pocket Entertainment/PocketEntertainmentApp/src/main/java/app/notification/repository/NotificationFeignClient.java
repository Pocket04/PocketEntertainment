package app.notification.repository;

import app.notification.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "notification-svc", url = "localhost:8081/api/v1/notifications")
public interface NotificationFeignClient {

    @PostMapping
    ResponseEntity<NotificationRequest> sendNotification(@RequestBody NotificationRequest notificationRequest);

    @GetMapping
    ResponseEntity<List<NotificationRequest>> getNotifications();
}
