package myrzakhan_taskflow.entities.elastic;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "task_indexes")
public class TaskIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private TaskStatus status;

    @Field(type = FieldType.Keyword)
    private Priority priority;

    @Field(type = FieldType.Date)
    private LocalDate deadline;

    @Field(type = FieldType.Long)
    private Long assignedUserId;

    @Field(type = FieldType.Long)
    private Long projectId;
}
