package myrzakhan_taskflow.services.mongo;

import com.mongodb.client.gridfs.model.GridFSFile;
import myrzakhan_taskflow.config.UploadFileValidationProperties;
import myrzakhan_taskflow.entities.mongo.DocumentFile;
import myrzakhan_taskflow.exceptions.FileSizeExceededException;
import myrzakhan_taskflow.exceptions.InvalidMimeTypeException;
import myrzakhan_taskflow.repositories.mongo.DocumentFileRepository;
import myrzakhan_taskflow.services.mongo.impl.DocumentFileServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentFileServiceTest {

    @Mock
    private DocumentFileRepository documentFileRepository;

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private UploadFileValidationProperties uploadFileValidation;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private DocumentFileServiceImpl documentFileService;

    private ObjectId objectId;

    @BeforeEach
    void setUp() {
        objectId = new ObjectId();
    }

    @Test
    void testDownloadDocument() {
        var mockFile = mock(GridFSFile.class);
        when(gridFsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)))).thenReturn(mockFile);

        var result = documentFileService.downloadDocument(objectId.toString());

        assertThat(result).isEqualTo(mockFile);
        verify(gridFsTemplate, times(1)).findOne(new Query(Criteria.where("_id").is(objectId)));
    }

    @Test
    void testUploadDocument_success() throws Exception {
        long taskId = 1L;
        ObjectId id = ObjectId.get();
        String filename = "test.txt";
        String contentType = "application/pdf";
        byte[] content = "test".getBytes();
        long size = content.length;

        when(multipartFile.getSize()).thenReturn(size);
        when(uploadFileValidation.getMaxSize()).thenReturn(1024 * 1024L);
        when(multipartFile.getContentType()).thenReturn(contentType);
        when(uploadFileValidation.getAllowedMimeTypes()).thenReturn(Set.of("application/pdf", "image/jpeg", "image/png"));
        when(multipartFile.getOriginalFilename()).thenReturn(filename);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(gridFsTemplate.store(any(InputStream.class), eq(filename), eq(contentType))).thenReturn(id);

        DocumentFile savedFile = new DocumentFile();
        savedFile.setId(id);
        savedFile.setTaskId(taskId);
        savedFile.setFilename(filename);
        savedFile.setFileType(contentType);
        savedFile.setSize(size);

        when(documentFileRepository.save(any(DocumentFile.class))).thenReturn(savedFile);

        var result = documentFileService.uploadDocument(taskId, multipartFile);

        assertEquals(id, result.getId());
        assertEquals(taskId, result.getTaskId());
        assertEquals(filename, result.getFilename());
        assertEquals(contentType, result.getFileType());
        assertEquals(size, result.getSize());

        verify(gridFsTemplate).store(any(InputStream.class), eq(filename), eq(contentType));
        verify(documentFileRepository).save(any(DocumentFile.class));
    }

    @Test
    void testUploadDocument_fileTooLarge() {

        when(multipartFile.getSize()).thenReturn(2048L);
        when(uploadFileValidation.getMaxSize()).thenReturn(1024L);

        assertThrows(FileSizeExceededException.class, () -> documentFileService.uploadDocument(1L, multipartFile));

        verify(multipartFile).getSize();
        verify(uploadFileValidation).getMaxSize();
        verifyNoMoreInteractions(gridFsTemplate, documentFileRepository);
    }

    @Test
    void testUploadDocument_invalidMimeType() {
        when(multipartFile.getContentType()).thenReturn("image/gif");
        when(uploadFileValidation.getAllowedMimeTypes()).thenReturn(Set.of("application/pdf", "image/jpeg", "image/png"));

        assertThrows(InvalidMimeTypeException.class, () -> documentFileService.uploadDocument(1L, multipartFile));
    }

    @Test
    void testDeleteDocument() {
        var id = String.valueOf(ObjectId.get());

        documentFileService.deleteDocumentById(id);

        verify(documentFileRepository).deleteById(id);
        verifyNoMoreInteractions(documentFileRepository);
    }
}
