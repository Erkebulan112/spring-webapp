package myrzakhan_taskflow.entities.postgres;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import myrzakhan_taskflow.entities.enums.ProjectStatus;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Project extends BaseEntity {

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User user;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users = new HashSet<>();
}
