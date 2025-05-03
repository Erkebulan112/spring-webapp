package myrzakhan_taskflow.services.mongo.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.config.UploadFileValidationProperties;
import myrzakhan_taskflow.entities.mongo.DocumentFile;
import myrzakhan_taskflow.exceptions.FileSizeExceededException;
import myrzakhan_taskflow.exceptions.InvalidMimeTypeException;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.repositories.mongo.DocumentFileRepository;
import myrzakhan_taskflow.services.mongo.DocumentFileService;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentFileServiceImpl implements DocumentFileService {

    private final DocumentFileRepository documentRepository;
    private final GridFsTemplate gridFsTemplate;
    private final UploadFileValidationProperties uploadFileValidationProperties;

    @Override
    public List<DocumentFile> getDocumentByTask(Long taskId) {
        log.info("Get documents by task");
        var documents = documentRepository.findByTaskId(taskId);

        if (documents.isEmpty()) {
            throw new NotFoundException("Documents for task with id %d not found".formatted(taskId));
        }
        return documentRepository.findByTaskId(taskId);
    }

    @Override
    public GridFSFile downloadDocument(String id) {
        log.info("Download document by id {}", id);
        ObjectId objectId = new ObjectId(id);
        var file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)));

        if (file == null) {
            log.error("Document with id {} not found", id);
            throw new NotFoundException("Document not found with id %s".formatted(id));
        }

        return file;
    }

    @Override
    public Resource getResource(GridFSFile file) {
        return gridFsTemplate.getResource(file);
    }

    @Override
    public DocumentFile uploadDocument(Long taskId, MultipartFile multipartFile) throws IOException {
        log.info("Upload document by task id {}", taskId);

        if (multipartFile.getSize() > uploadFileValidationProperties.getMaxSize()) {
            log.error("File is too large");
            throw new FileSizeExceededException("File is too large");
        }

        if (!uploadFileValidationProperties.getAllowedMimeTypes().contains(multipartFile.getContentType())) {
            log.error("File content type is not supported");
            throw new InvalidMimeTypeException("Files content type must be: %s".formatted(uploadFileValidationProperties.getAllowedMimeTypes()));
        }

        ObjectId fileId = gridFsTemplate.store(
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType()
        );

        DocumentFile file = new DocumentFile();
        file.setId(fileId);
        file.setTaskId(taskId);
        file.setFilename(multipartFile.getOriginalFilename());
        file.setFileType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());

        return documentRepository.save(file);
    }

    @Override
    public void deleteDocumentById(String id) {
        log.info("Delete document by id {}", id);
        ObjectId objectId = new ObjectId(id);
        documentRepository.deleteById(objectId);
    }
}
