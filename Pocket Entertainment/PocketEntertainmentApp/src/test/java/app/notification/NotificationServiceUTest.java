package app.notification;


import app.notification.dto.NotificationRequest;
import app.notification.repository.NotificationFeignClient;
import app.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.Notification;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {
    @Mock
    private NotificationFeignClient notificationFeignClient;
    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendNotification_Success() {
        NotificationRequest dto = new NotificationRequest();
        UUID id = UUID.randomUUID();

        notificationService.sendNotification(dto, id);

        verify(notificationFeignClient, times(1)).sendNotification(dto);
    }

}
