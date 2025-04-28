package notification_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification_service.dto.NotificationEvent;
import notification_service.entity.Notification;
import notification_service.repository.NotificationRepository;
import notification_service.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification save(NotificationEvent request) {
        log.info("Save notification request: {}", request);
        Notification notification = new Notification();
        notification.setTaskId(request.taskId());
        notification.setTitle(request.title());
        notification.setStatus(request.status());
        return notificationRepository.save(notification);
    }
}
