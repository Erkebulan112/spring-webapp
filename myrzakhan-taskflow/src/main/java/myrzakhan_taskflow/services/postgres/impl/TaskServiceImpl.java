package myrzakhan_taskflow.services.postgres.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.config.RabbitMQConfig;
import myrzakhan_taskflow.dtos.event.TaskHistoryEvent;
import myrzakhan_taskflow.dtos.requests.TaskCreateRequest;
import myrzakhan_taskflow.dtos.requests.TaskUpdateRequest;
import myrzakhan_taskflow.entities.postgres.Task;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.message.TaskHistoryEventProducer;
import myrzakhan_taskflow.repositories.postgres.TaskRepository;
import myrzakhan_taskflow.services.postgres.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryEventProducer producer;

    @Override
    @Transactional(readOnly = true)
    public Page<Task> findAllTasks(Pageable pageable) {
        log.info("Get all tasks");
        return taskRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findTaskById(Long id) {
        log.info("Get task by id: {}", id);
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id %d".formatted(id)));
    }

    @Override
    public Task createTask(TaskCreateRequest request) {
        log.info("Creating task: {}", request);
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDeadline(request.deadline());

        var savedTask = taskRepository.save(task);
        sendTaskEvent(task, "CREATE", createTaskDetails(task));
        return savedTask;
    }

    @Override
    public Task updateTask(Long id, TaskUpdateRequest request) {
        log.info("Updating task: {}", request);
        var task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id %d".formatted(id)));
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDeadline(request.deadline());

        var updatedTask = taskRepository.save(task);
        sendTaskEvent(task, "UPDATE", updateTaskDetails(task, request));
        return updatedTask;
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task: {}", id);
        taskRepository.deleteById(id);
    }

    private void sendTaskEvent(Task task, String action, Map<String, Object> details) {
        TaskHistoryEvent event = TaskHistoryEvent.builder()
                .taskId(task.getId())
                .action(action)
                .details(details)
                .build();

        log.info("Sending task event: {}", event);
        producer.sendTaskHistoryEvent(RabbitMQConfig.TASK_HISTORY_EXCHANGE, RabbitMQConfig.TASK_HISTORY_ROUTING_KEY, event);
    }

    private Map<String, Object> createTaskDetails(Task task) {
        return Map.of(
                "title", task.getTitle(),
                "description", task.getDescription(),
                "status", task.getStatus(),
                "priority", task.getPriority(),
                "deadline", task.getDeadline()
        );
    }

    private Map<String, Object> updateTaskDetails(Task task, TaskUpdateRequest request) {
        Map<String, Object> changes = new HashMap<>();

        if (!task.getTitle().equals(request.title())) {
            changes.put("title", Map.of("old", task.getTitle(), "new", request.title()));
        }
        if (!task.getDescription().equals(request.description())) {
            changes.put("description", Map.of("old", task.getDescription(), "new", request.description()));
        }
        if (!task.getStatus().equals(request.status())) {
            changes.put("status", Map.of("old", task.getStatus(), "new", request.status()));
        }
        if (!task.getPriority().equals(request.priority())) {
            changes.put("priority", Map.of("old", task.getPriority(), "new", request.priority()));
        }
        if (!task.getDeadline().equals(request.deadline())) {
            changes.put("deadline", Map.of("old", task.getDeadline(), "new", request.deadline()));
        }

        return changes;
    }
}
