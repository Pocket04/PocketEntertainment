package app.notification.repository;

import app.notification.dto.NotificationRequest;
import app.notification.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "notification-svc", url = "localhost:8081/api/v1/notifications")
public interface NotificationFeignClient {

    @PostMapping
    ResponseEntity<NotificationRequest> sendNotification(@RequestBody NotificationRequest notificationRequest);

    @GetMapping("/{id}")
    ResponseEntity<List<NotificationResponse>> getNotifications(@PathVariable UUID id);
}
