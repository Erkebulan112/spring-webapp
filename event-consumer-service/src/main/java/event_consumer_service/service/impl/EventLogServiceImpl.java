package event_consumer_service.service.impl;

import event_consumer_service.entity.EventLog;
import event_consumer_service.repository.EventLogRepository;
import event_consumer_service.service.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventLogServiceImpl implements EventLogService {

    private final EventLogRepository eventLogRepository;

    @Override
    public Page<EventLog> getAll(Pageable pageable) {
        return eventLogRepository.findAll(pageable);
    }

    @Override
    public EventLog save(EventLog eventLog) {
        return eventLogRepository.save(eventLog);
    }
}
