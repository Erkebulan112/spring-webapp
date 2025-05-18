package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.Comment;

public record CommentCreateResponse(
        Long id,
        String content,
        Long taskId,
        Long userId)
{
    public static CommentCreateResponse toDto(Comment comment) {
        return new CommentCreateResponse(comment.getId(), comment.getContent(),
                comment.getTask().getId(), comment.getUser().getId());
    }
}
