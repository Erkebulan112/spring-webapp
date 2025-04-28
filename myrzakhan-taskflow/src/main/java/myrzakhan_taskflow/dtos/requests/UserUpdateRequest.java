package myrzakhan_taskflow.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(

        @NotBlank(message = "Name is required")
        String firstname,

        String lastname,

        @Email(message = "Incorrect format email")
        String email) {
}
