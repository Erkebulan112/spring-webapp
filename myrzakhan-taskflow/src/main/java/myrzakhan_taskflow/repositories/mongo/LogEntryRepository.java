package myrzakhan_taskflow.repositories.mongo;

import myrzakhan_taskflow.entities.mongo.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, String> {
    List<LogEntry> findByLevel(String level);
}
