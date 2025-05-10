package myrzakhan_taskflow.repository;

import myrzakhan_taskflow.dtos.event.NotificationStatus;
import myrzakhan_taskflow.entities.mongo.TaskHistory;
import myrzakhan_taskflow.repositories.mongo.TaskHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class TaskHistoryRepositoryTest {

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    private final Long taskId = 1L;
    private TaskHistory taskHistory1;
    private TaskHistory taskHistory2;

    @BeforeEach
    void setUp() {
        taskHistoryRepository.deleteAll();

        taskHistory1 = new TaskHistory();
        taskHistory1.setTaskId(taskId);
        taskHistory1.setAction(NotificationStatus.CREATE);
        taskHistory1.setPerformedBy(1L);

        taskHistory2 = new TaskHistory();
        taskHistory2.setTaskId(taskId);
        taskHistory2.setAction(NotificationStatus.UPDATE);
        taskHistory2.setPerformedBy(2L);

        taskHistoryRepository.saveAll(List.of(taskHistory1, taskHistory2));
    }

    @Test
    void whenFindByTaskId_thenReturnTaskHistory() {

        var result = taskHistoryRepository.findByTaskId(taskId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting("action").containsExactlyInAnyOrder(NotificationStatus.CREATE, NotificationStatus.UPDATE);
    }

    @Test
    void whenFindNonExistentTaskId_thenReturnEmptyList() {

        var nonExistentTaskId = 999L;
        var result = taskHistoryRepository.findByTaskId(nonExistentTaskId);

        assertThat(result).isEmpty();
    }
}
