package myrzakhan_taskflow.services.mongo;

import java.util.Map;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.entities.mongo.LogEntry;
import java.util.List;

public interface LogEntryService {

    List<LogEntry> getAll();

    List<LogEntry> getByLevel(String level);

    LogEntry createLogs(LogLevel level, String message, Map<String, Object> context);
}
