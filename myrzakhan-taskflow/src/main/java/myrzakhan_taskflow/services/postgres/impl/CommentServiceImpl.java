package myrzakhan_taskflow.services.postgres.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.requests.CommentCreateRequest;
import myrzakhan_taskflow.dtos.requests.CommentUpdateRequest;
import myrzakhan_taskflow.entities.postgres.Comment;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.repositories.postgres.CommentRepository;
import myrzakhan_taskflow.services.postgres.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> findAllComments(Pageable pageable) {
        log.info("Get all comments");
        return commentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findCommentById(Long id) {
        log.info("Get comment by id: {}", id);
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found with id: %d".formatted(id)));
    }

    @Override
    public Comment createComment(CommentCreateRequest request) {
        log.info("Create comment: {}", request);
        Comment comment = new Comment();
        comment.setContent(request.content());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, CommentUpdateRequest request) {
        log.info("Update comment: {}", request);
        var comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found with id: %d".formatted(id)));
        comment.setContent(request.content());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long id) {
        log.info("Delete comment: {}", id);
        commentRepository.deleteById(id);
    }
}
