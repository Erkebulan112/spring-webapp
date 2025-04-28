package myrzakhan_taskflow.services.mongo;

import com.mongodb.client.gridfs.model.GridFSFile;
import myrzakhan_taskflow.entities.mongo.DocumentFile;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface DocumentFileService {

    List<DocumentFile> getDocumentByTask(Long taskId);

    GridFSFile downloadDocument(String id);

    Resource getResource(GridFSFile file);

    DocumentFile uploadDocument(Long taskId, MultipartFile multipartFile) throws IOException;

    void deleteDocumentById(String id);
}
