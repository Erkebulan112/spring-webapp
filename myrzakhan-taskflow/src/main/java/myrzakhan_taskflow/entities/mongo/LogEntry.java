package myrzakhan_taskflow.entities.mongo;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import myrzakhan_taskflow.dtos.event.LogLevel;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "log_entries")
@Getter
@Setter
public class LogEntry {

    @Id
    private ObjectId id;

    private LogLevel level;

    private String message;

    private Map<String, Object> context;

    @CreatedDate
    private LocalDateTime timestamp;
}
