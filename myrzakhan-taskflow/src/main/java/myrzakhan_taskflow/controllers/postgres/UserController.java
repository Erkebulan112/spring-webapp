package myrzakhan_taskflow.controllers.postgres;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.controllers.PageableConstants;
import myrzakhan_taskflow.dtos.requests.UserCreateRequest;
import myrzakhan_taskflow.dtos.requests.UserUpdateRequest;
import myrzakhan_taskflow.dtos.responses.UserCreateResponse;
import myrzakhan_taskflow.dtos.responses.UserResponse;
import myrzakhan_taskflow.dtos.responses.UserUpdateResponse;
import myrzakhan_taskflow.services.postgres.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(
                    size = PageableConstants.DEFAULT_SIZE,
                    page = PageableConstants.DEFAULT_PAGE,
                    sort = PageableConstants.DEFAULT_SORT_BY,
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        var responses = userService.findAllUsers(pageable)
                .map(UserResponse::toDto);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        var response = userService.findUserById(id);
        return ResponseEntity.ok(UserResponse.toDto(response));
    }

    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        var response = userService.createUser(request);
        return ResponseEntity.created(URI.create("/users/")).body(UserCreateResponse.toDto(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        var response = userService.updateUser(id, request);
        return ResponseEntity.ok(UserUpdateResponse.toDto(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
