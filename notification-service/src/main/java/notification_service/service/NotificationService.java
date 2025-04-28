package notification_service.service;

import notification_service.dto.NotificationEvent;
import notification_service.entity.Notification;

public interface NotificationService {

    Notification save(NotificationEvent request);
}
