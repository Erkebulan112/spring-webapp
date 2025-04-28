package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.User;
import java.time.LocalDateTime;

public record UserResponse (
        Long id,
        String firstName,
        String lastName,
        String email,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
{
    public static UserResponse toDto(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.isActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
