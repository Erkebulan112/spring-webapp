package myrzakhan_taskflow.dtos.responses;

import myrzakhan_taskflow.entities.postgres.User;
import java.time.LocalDateTime;

public record UserUpdateResponse (
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime updatedAt)
{
    public static UserUpdateResponse toDto(User user) {
        return new UserUpdateResponse(user.getId(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getUpdatedAt());
    }
}