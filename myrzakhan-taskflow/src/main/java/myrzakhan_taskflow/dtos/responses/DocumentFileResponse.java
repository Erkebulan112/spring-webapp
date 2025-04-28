package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.mongo.DocumentFile;
import java.time.LocalDateTime;

public record DocumentFileResponse(
        String id,
        Long taskId,
        String filename,
        String fileType,
        LocalDateTime uploadedAt,
        Long size)
{
    public static DocumentFileResponse toDto(DocumentFile document) {
        return new DocumentFileResponse(document.getId().toHexString(), document.getTaskId(), document.getFilename(),
                document.getFileType(), document.getUploadedAt(), document.getSize());
    }
}
