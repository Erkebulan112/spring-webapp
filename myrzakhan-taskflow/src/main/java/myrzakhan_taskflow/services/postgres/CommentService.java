package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.dtos.requests.CommentCreateRequest;
import myrzakhan_taskflow.dtos.requests.CommentUpdateRequest;
import myrzakhan_taskflow.entities.postgres.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<Comment> findAllComments(Pageable pageable);

    Comment findCommentById(Long id);

    Comment createComment(CommentCreateRequest comment);

    Comment updateComment(Long id, CommentUpdateRequest comment);

    void deleteComment(Long id);
}
