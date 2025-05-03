package myrzakhan_taskflow.repositories.mongo;

import myrzakhan_taskflow.entities.mongo.LogEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, ObjectId> {
    List<LogEntry> findByLevel(String level);
}
