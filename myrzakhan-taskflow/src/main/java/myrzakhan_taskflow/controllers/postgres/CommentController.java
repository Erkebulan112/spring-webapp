package myrzakhan_taskflow.controllers.postgres;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.controllers.PageableConstants;
import myrzakhan_taskflow.dtos.requests.CommentCreateRequest;
import myrzakhan_taskflow.dtos.requests.CommentUpdateRequest;
import myrzakhan_taskflow.dtos.responses.CommentCreateResponse;
import myrzakhan_taskflow.dtos.responses.CommentResponse;
import myrzakhan_taskflow.dtos.responses.CommentUpdateResponse;
import myrzakhan_taskflow.services.postgres.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getAllComments(
            @PageableDefault(
                    size = PageableConstants.DEFAULT_SIZE,
                    page = PageableConstants.DEFAULT_PAGE,
                    sort = PageableConstants.DEFAULT_SORT_BY,
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        var responses = commentService.findAllComments(pageable).map(CommentResponse::toDto);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        var response = commentService.findCommentById(id);
        return ResponseEntity.ok(CommentResponse.toDto(response));
    }

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(@Valid @RequestBody CommentCreateRequest request) {
        var response = commentService.createComment(request);
        return ResponseEntity.created(URI.create("/comments/")).body(CommentCreateResponse.toDto(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentUpdateResponse> updateComment(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request) {
        var response = commentService.updateComment(id, request);
        return ResponseEntity.ok(CommentUpdateResponse.toDto(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
