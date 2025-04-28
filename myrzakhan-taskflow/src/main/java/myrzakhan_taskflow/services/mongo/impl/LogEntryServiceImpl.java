package myrzakhan_taskflow.services.mongo.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.entities.mongo.LogEntry;
import myrzakhan_taskflow.repositories.mongo.LogEntryRepository;
import myrzakhan_taskflow.services.mongo.LogEntryService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

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
    public LogEntry createLogs(LogEntry logEntry) {
        log.info("Create log {}", logEntry);
        LogEntry log = new LogEntry();
        log.setId(logEntry.getId());
        log.setLevel(logEntry.getLevel());
        log.setMessage(logEntry.getMessage());
        log.setTimestamp(LocalDateTime.now());
        log.setContext(logEntry.getContext());
        return logEntryRepository.save(log);
    }
}
