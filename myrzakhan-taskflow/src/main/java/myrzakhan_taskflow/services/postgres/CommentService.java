package myrzakhan_taskflow.services.postgres;

import java.util.List;
import myrzakhan_taskflow.dtos.requests.CommentCreateRequest;
import myrzakhan_taskflow.dtos.requests.CommentUpdateRequest;
import myrzakhan_taskflow.entities.elastic.CommentIndex;
import myrzakhan_taskflow.entities.postgres.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<Comment> findAllComments(Pageable pageable);

    Comment findCommentById(Long id);

    Comment createComment(Long taskId, CommentCreateRequest comment);

    Comment updateComment(Long id, CommentUpdateRequest comment);

    void deleteComment(Long id);

    List<CommentIndex> searchComments(String query, Long taskId);
}
