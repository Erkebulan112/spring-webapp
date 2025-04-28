package myrzakhan_taskflow.repositories.mongo;

import myrzakhan_taskflow.entities.mongo.TaskHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskHistoryRepository extends MongoRepository<TaskHistory, String> {
    List<TaskHistory> findByTaskId(Long taskId);
}
