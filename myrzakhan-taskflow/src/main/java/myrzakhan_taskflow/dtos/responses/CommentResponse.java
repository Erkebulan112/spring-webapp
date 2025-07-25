package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.Comment;
import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        Long taskId,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
{
    public static CommentResponse toDto(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getTask().getId(), comment.getUser().getId(),
                comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
