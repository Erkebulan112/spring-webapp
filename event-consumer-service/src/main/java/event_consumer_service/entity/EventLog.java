package event_consumer_service.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Document(collection = "events")
public class EventLog {

    @Id
    private ObjectId id;
    private String eventType;
    private String entityId;
    private String entityType;
    private Map<String, Object> payload;

    @CreatedDate
    private LocalDateTime createdAt;
}
