package myrzakhan_taskflow.services.mongo.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.entities.mongo.LogEntry;
import myrzakhan_taskflow.repositories.mongo.LogEntryRepository;
import myrzakhan_taskflow.services.mongo.LogEntryService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogEntryServiceImpl implements LogEntryService {

    private final LogEntryRepository logEntryRepository;

    @Override
    public List<LogEntry> getAll() {
        log.info("Get all logs");
        return logEntryRepository.findAll();
    }

    @Override
    public List<LogEntry> getByLevel(String level) {
        log.info("Get all logs by level {}", level);
        return logEntryRepository.findByLevel(level);
    }

    @Override
    public LogEntry createLogs(LogLevel level, String message, Map<String, Object> context) {
        log.info("Create log with level {}, message {}, context {}", level, message, context);
        LogEntry log = new LogEntry();
        log.setLevel(level);
        log.setMessage(message);
        log.setContext(context);
        return logEntryRepository.save(log);
    }
}
