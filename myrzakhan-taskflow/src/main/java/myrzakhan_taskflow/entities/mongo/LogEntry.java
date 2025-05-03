package myrzakhan_taskflow.entities.mongo;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class LogEntry {

    @Id
    private ObjectId id;

    private String level;

    private String message;

    @CreatedDate
    private LocalDateTime timestamp;

    private Map<String, Object> context;
}
