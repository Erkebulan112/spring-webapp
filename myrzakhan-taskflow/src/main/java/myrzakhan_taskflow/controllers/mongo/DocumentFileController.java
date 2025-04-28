package myrzakhan_taskflow.controllers.mongo;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.dtos.responses.DocumentFileResponse;
import myrzakhan_taskflow.services.mongo.DocumentFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentFileController {

    private final DocumentFileService documentFileService;

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<List<DocumentFileResponse>> getDocumentFilesByTaskId(@PathVariable Long taskId) {
        var responses = documentFileService.getDocumentByTask(taskId)
                .stream().map(DocumentFileResponse::toDto).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String id) {
        var file = documentFileService.downloadDocument(id);
        var resource = documentFileService.getResource(file);
        var contentType = Optional.ofNullable(file.getMetadata())
                .map(meta -> meta.getString("_contentType"))
                .orElse("application/octet-stream");

        var encodedFilename = URLEncoder.encode(file.getFilename(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                .body(resource);
    }

    @PostMapping("/tasks/{taskId}")
    public ResponseEntity<DocumentFileResponse> uploadFile(@PathVariable Long taskId,
                                                           @RequestParam MultipartFile file) throws IOException {
        var response = documentFileService.uploadDocument(taskId, file);
        return ResponseEntity.created(URI.create("/documents/")).body(DocumentFileResponse.toDto(response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentFileService.deleteDocumentById(id);
        return ResponseEntity.noContent().build();
    }
}
