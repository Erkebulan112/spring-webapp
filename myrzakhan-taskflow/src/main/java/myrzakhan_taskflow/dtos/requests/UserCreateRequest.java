package myrzakhan_taskflow.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(

        @NotBlank(message = "Name is required")
        String firstName,

        String lastName,

        @Email(message = "Incorrect format email")
        String email) {
}
