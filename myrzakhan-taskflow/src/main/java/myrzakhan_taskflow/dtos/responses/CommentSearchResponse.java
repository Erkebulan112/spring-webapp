package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.elastic.CommentIndex;
import java.time.LocalDate;

public record CommentSearchResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        LocalDate createdAt)
{
    public static CommentSearchResponse toDto(CommentIndex commentIndex) {
        return new CommentSearchResponse(commentIndex.getId(), commentIndex.getContent(), commentIndex.getTaskId(),
                commentIndex.getUserId(), commentIndex.getCreatedAt());
    }
}
