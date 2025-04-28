package myrzakhan_taskflow.services.mongo;

import myrzakhan_taskflow.entities.mongo.LogEntry;
import java.util.List;

public interface LogEntryService {

    List<LogEntry> getAll();

    List<LogEntry> getByLevel(String level);

    LogEntry createLogs(LogEntry logEntry);
}
