package myrzakhan_taskflow.services.postgres.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.event.CommentIndexDelete;
import myrzakhan_taskflow.dtos.event.CommentIndexEvent;
import myrzakhan_taskflow.dtos.requests.CommentCreateRequest;
import myrzakhan_taskflow.dtos.requests.CommentUpdateRequest;
import myrzakhan_taskflow.entities.elastic.CommentIndex;
import myrzakhan_taskflow.entities.postgres.Comment;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.message.KafkaLogProducer;
import myrzakhan_taskflow.repositories.postgres.CommentRepository;
import myrzakhan_taskflow.services.postgres.CommentService;
import myrzakhan_taskflow.services.postgres.TaskService;
import myrzakhan_taskflow.services.postgres.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;
    private final KafkaLogProducer kafkaLogProducer;
    private final ElasticsearchOperations elasticsearchOperations;

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
    public Comment createComment(Long taskId, CommentCreateRequest request) {
        log.info("Create comment: {}", request);
        var task = taskService.findTaskById(taskId);
        var user = userService.findUserById(request.userId());
        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setTask(task);
        comment.setUser(user);
        commentRepository.save(comment);

        kafkaLogProducer.sendIndexEvent(CommentIndexEvent.toDto(comment));
        return comment;
    }

    @Override
    public Comment updateComment(Long id, CommentUpdateRequest request) {
        log.info("Update comment: {}", request);
        var comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found with id: %d".formatted(id)));
        comment.setContent(request.content());
        commentRepository.save(comment);

        kafkaLogProducer.sendIndexEvent(CommentIndexEvent.toDto(comment));
        return comment;
    }

    @Override
    public void deleteComment(Long id) {
        log.info("Delete comment: {}", id);
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found with id: %d".formatted(id)));

        kafkaLogProducer.sendIndexEvent(new CommentIndexDelete(comment.getId()));
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentIndex> searchComments(String query, Long taskId) {
        log.info("Search comments with query: {} and taskId: {}" , query, taskId);

        Criteria criteria = new Criteria("content")
                .contains(query).and("taskId").is(taskId);

        var searchQuery = new NativeQueryBuilder()
                .withQuery(new CriteriaQuery(criteria))
                .build();

        SearchHits<CommentIndex> searchHits = elasticsearchOperations.search(searchQuery, CommentIndex.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
