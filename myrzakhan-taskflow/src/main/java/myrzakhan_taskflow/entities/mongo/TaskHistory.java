package myrzakhan_taskflow.entities.mongo;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document
@Getter
@Setter
public class TaskHistory {

    @Id
    private ObjectId id;

    private Long taskId;

    private String action;

    private Long performedBy;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private Map<String, Object> details;
}
