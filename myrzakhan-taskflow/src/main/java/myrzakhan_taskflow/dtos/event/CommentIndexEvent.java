package myrzakhan_taskflow.dtos.event;

import java.time.LocalDate;
import myrzakhan_taskflow.entities.elastic.CommentIndex;
import myrzakhan_taskflow.entities.postgres.Comment;

public record CommentIndexEvent(
        Long id,
        String content,
        Long taskId,
        Long userId,
        LocalDate createdAt
) implements IndexingEvent {
    public static CommentIndexEvent toDto(Comment comment) {
        return new CommentIndexEvent(
                comment.getId(),
                comment.getContent(),
                comment.getTask() != null ? comment.getTask().getId() : null,
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getCreatedAt() != null ? comment.getCreatedAt().toLocalDate() : null
        );
    }

    public CommentIndex toIndex() {
        CommentIndex commentIndex = new CommentIndex();
        commentIndex.setId(id);
        commentIndex.setContent(content);
        commentIndex.setTaskId(taskId);
        commentIndex.setUserId(userId);
        commentIndex.setCreatedAt(createdAt);
        return commentIndex;
    }
}
