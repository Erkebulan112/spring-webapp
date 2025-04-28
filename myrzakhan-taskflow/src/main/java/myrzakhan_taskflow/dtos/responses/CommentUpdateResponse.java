package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.Comment;
import java.time.LocalDateTime;

public record CommentUpdateResponse(
        Long id,
        String content,
        LocalDateTime updatedAt)
{
    public static CommentUpdateResponse toDto(Comment comment) {
        return new CommentUpdateResponse(comment.getId(), comment.getContent(), comment.getUpdatedAt());
    }
}
