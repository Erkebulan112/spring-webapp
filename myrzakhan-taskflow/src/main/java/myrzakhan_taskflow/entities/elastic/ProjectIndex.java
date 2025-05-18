package myrzakhan_taskflow.entities.elastic;

import lombok.Getter;
import lombok.Setter;
import myrzakhan_taskflow.entities.enums.ProjectStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "project_indexes")
public class ProjectIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private ProjectStatus status;
}
