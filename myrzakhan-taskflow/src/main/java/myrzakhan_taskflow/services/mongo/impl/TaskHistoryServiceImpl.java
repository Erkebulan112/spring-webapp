package myrzakhan_taskflow.services.mongo.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.entities.mongo.TaskHistory;
import myrzakhan_taskflow.repositories.mongo.TaskHistoryRepository;
import myrzakhan_taskflow.services.mongo.TaskHistoryService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;

    @Override
    public List<TaskHistory> findTaskHistory(Long taskId) {
        log.info("Find task history by task id: {}", taskId);
        return taskHistoryRepository.findByTaskId(taskId);
    }

    @Override
    public TaskHistory save(Long taskId, String action, Long performedBy, Map<String, Object> details) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTaskId(taskId);
        taskHistory.setAction(action);
        taskHistory.setPerformedBy(performedBy);
        taskHistory.setDetails(details);
        return taskHistoryRepository.save(taskHistory);
    }
}
