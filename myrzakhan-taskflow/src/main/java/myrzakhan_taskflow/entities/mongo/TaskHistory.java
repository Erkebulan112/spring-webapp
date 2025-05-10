package myrzakhan_taskflow.entities.mongo;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import myrzakhan_taskflow.dtos.event.NotificationStatus;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task_histories")
@Getter
@Setter
public class TaskHistory {

    @Id
    private ObjectId id;

    private Long taskId;

    private NotificationStatus action;

    private Long performedBy;

    @CreatedDate
    private LocalDateTime timestamp;

    private Map<String, Object> details;
}
