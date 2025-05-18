package myrzakhan_taskflow.entities.mongo;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "document_files")
public class DocumentFile {

    @Id
    private ObjectId id;

    private Long taskId;

    private String filename;

    private String fileType;

    @CreatedDate
    private LocalDateTime uploadedAt;

    private Long size;
}
