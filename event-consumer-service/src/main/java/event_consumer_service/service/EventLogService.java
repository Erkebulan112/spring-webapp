package event_consumer_service.service;

import event_consumer_service.entity.EventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventLogService {

    Page<EventLog> getAll(Pageable pageable);

    EventLog save(EventLog eventLog);

}
