package app.notification.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationRequest {

    private String subject;

    private String body;

    private UUID userId;

    private String replyTo;

}
