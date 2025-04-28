package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.User;
import java.time.LocalDateTime;

public record UserCreateResponse (
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt)
{
    public static UserCreateResponse toDto(User user) {
        return new UserCreateResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getCreatedAt());
    }
}
