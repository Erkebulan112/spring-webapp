package myrzakhan_taskflow.services.postgres.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.config.RabbitMQConfig;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.dtos.event.NotificationStatus;
import myrzakhan_taskflow.dtos.event.TaskHistoryEvent;
import myrzakhan_taskflow.dtos.event.TaskIndexDelete;
import myrzakhan_taskflow.dtos.event.TaskIndexEvent;
import myrzakhan_taskflow.dtos.event.TaskNotificationEvent;
import myrzakhan_taskflow.dtos.requests.TaskCreateRequest;
import myrzakhan_taskflow.dtos.requests.TaskUpdateRequest;
import myrzakhan_taskflow.entities.elastic.TaskIndex;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import myrzakhan_taskflow.entities.postgres.Task;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.message.KafkaLogProducer;
import myrzakhan_taskflow.message.TaskHistoryEventProducer;
import myrzakhan_taskflow.repositories.postgres.TaskRepository;
import myrzakhan_taskflow.services.postgres.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
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
    private final KafkaLogProducer kafkaLogProducer;
    private final ElasticsearchOperations elasticsearchOperations;
    private static final String TASKFLOW_DIRECT_EXCHANGE = "taskflow.direct.exchange";
    private static final String TASK_NOTIFICATION_DIRECT_RK = "notification.routing.key";

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

        kafkaLogProducer.sendLog(LogLevel.INFO, "Task created", buildContext(savedTask));
        kafkaLogProducer.sendTaskLog(task, "Task created");
        kafkaLogProducer.sendIndexEvent(TaskIndexEvent.toDto(task));
        sendTaskEvent(task, NotificationStatus.CREATE, createTaskDetails(task));
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

        kafkaLogProducer.sendLog(LogLevel.INFO, "Task updated", buildContext(updatedTask));
        kafkaLogProducer.sendTaskLog(task, "Task updated");
        kafkaLogProducer.sendIndexEvent(TaskIndexEvent.toDto(task));
        sendTaskEvent(task, NotificationStatus.UPDATE, updateTaskDetails(task, request));
        return updatedTask;
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task: {}", id);

        var task = taskRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Task not found with id %d".formatted(id)));

        Map<String, Object> context = new HashMap<>();
        context.put("title", task.getTitle());
        context.put("description", task.getDescription());
        context.put("status", task.getStatus());
        context.put("priority", task.getPriority());
        context.put("deadline", task.getDeadline());

        sendTaskEvent(task, NotificationStatus.DELETE, context);
        kafkaLogProducer.sendLog(LogLevel.INFO, "Task deleted", buildContext(task));
        kafkaLogProducer.sendTaskLog(task, "Task deleted");
        kafkaLogProducer.sendIndexEvent(new TaskIndexDelete(task.getId()));
        taskRepository.delete(task);
    }

    @Override
    public List<TaskIndex> searchTasks(String query, TaskStatus status, Priority priority) {

        Criteria titleCriteria = new Criteria("title").matches(query);
        Criteria descriptionCriteria = new Criteria("description").matches(query);

        Criteria contentCriteria = new Criteria().or(titleCriteria).or(descriptionCriteria);

        if (status != null) {
            contentCriteria  = contentCriteria.and(new Criteria("status").is(status));
        }

        if (priority != null) {
            contentCriteria = contentCriteria.and(new Criteria("priority").is(priority));
        }

        CriteriaQuery searchQuery = new CriteriaQuery(contentCriteria);

        SearchHits<TaskIndex> searchHits = elasticsearchOperations.search(searchQuery, TaskIndex.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
    }


    private void sendTaskEvent(Task task, NotificationStatus action, Map<String, Object> details) {
        TaskHistoryEvent event = TaskHistoryEvent.builder()
                .taskId(task.getId())
                .action(action)
                .details(details)
                .build();

        TaskNotificationEvent notificationEvent = TaskNotificationEvent.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .status(action)
                .build();

        log.info("Sending task event: {}", event);
        producer.sendTaskHistoryEvent(RabbitMQConfig.TASK_HISTORY_EXCHANGE, RabbitMQConfig.TASK_HISTORY_ROUTING_KEY, event);
        producer.sendTaskHistoryToNotification(TASKFLOW_DIRECT_EXCHANGE, TASK_NOTIFICATION_DIRECT_RK, notificationEvent);
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

    private Map<String, Object> buildContext(Task task) {
        Map<String, Object> context = new HashMap<>();
        context.put("taskId", task.getId());
        context.put("title", task.getTitle());
        context.put("status", task.getStatus());
        return context;
    }
}
