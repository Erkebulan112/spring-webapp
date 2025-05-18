package myrzakhan_taskflow.entities.elastic;

import jakarta.persistence.EntityListeners;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Document(indexName = "comment_indexes")
public class CommentIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Long)
    private Long taskId;

    @Field(type = FieldType.Long)
    private Long userId;

    @CreatedDate
    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private LocalDate createdAt;
}
