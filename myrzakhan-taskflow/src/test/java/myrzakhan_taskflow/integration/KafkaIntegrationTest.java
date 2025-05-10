package myrzakhan_taskflow.integration;

import myrzakhan_taskflow.config.KafkaConfig;
import myrzakhan_taskflow.dtos.event.LogLevel;
import myrzakhan_taskflow.dtos.responses.LogEntryResponse;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import myrzakhan_taskflow.repositories.mongo.LogEntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Testcontainers
public class KafkaIntegrationTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .withEmbeddedZookeeper();

    @Container
    static MongoDBContainer mongoContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.consumer.group-id", () -> "taskflow-log-group");

        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Test
    void shouldReceiveAndSaveLogEntry() {
        LogEntryResponse log = LogEntryResponse.builder()
                .level(LogLevel.INFO)
                .message("Task created")
                .context(Map.of("title", "Task",
                        "taskId", 7,
                        "status", TaskStatus.TODO))
                .build();

        kafkaTemplate.send(KafkaConfig.TASKFLOW_LOG_TOPIC, log);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            var saved = logEntryRepository.findAll()
                    .stream()
                    .filter(l -> l.getMessage().equals("Task created"))
                    .findFirst()
                    .orElse(null);

            assertThat(saved).isNotNull();
            assertThat(saved.getMessage()).isEqualTo("Task created");
        });
    }
}
