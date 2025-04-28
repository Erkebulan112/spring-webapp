package myrzakhan_taskflow.entities.postgres;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Task extends BaseEntity {

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User user;
}
