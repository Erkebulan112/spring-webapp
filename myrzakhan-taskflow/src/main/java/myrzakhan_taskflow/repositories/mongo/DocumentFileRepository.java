package myrzakhan_taskflow.repositories.mongo;

import myrzakhan_taskflow.entities.mongo.DocumentFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentFileRepository extends MongoRepository<DocumentFile, ObjectId> {
    List<DocumentFile> findByTaskId(Long taskId);
}
