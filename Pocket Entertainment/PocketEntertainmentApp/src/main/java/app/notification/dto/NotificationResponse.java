package app.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {

    private UUID userId;

    private String subject;

    private String body;

    private String status;

    private LocalDateTime date;
}
