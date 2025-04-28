package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.dtos.requests.TaskCreateRequest;
import myrzakhan_taskflow.dtos.requests.TaskUpdateRequest;
import myrzakhan_taskflow.entities.postgres.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<Task> findAllTasks(Pageable pageable);

    Task findTaskById(Long id);

    Task createTask(TaskCreateRequest task);

    Task updateTask(Long id, TaskUpdateRequest task);

    void deleteTask(Long id);
}
