package myrzakhan_taskflow.services.mongo;

import myrzakhan_taskflow.entities.mongo.TaskHistory;
import java.util.List;
import java.util.Map;

public interface TaskHistoryService {

    List<TaskHistory> findTaskHistory(Long taskId);

    TaskHistory save(Long taskId, String action, Long performedBy, Map<String, Object> details);
}
