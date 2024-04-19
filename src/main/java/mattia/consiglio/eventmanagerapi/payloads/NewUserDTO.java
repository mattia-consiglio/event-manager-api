package mattia.consiglio.eventmanagerapi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 3, message = "Username must be at least 3 characters long")
        String username,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,
        @NotBlank(message = "Password is required")
        @Size(min = 15, message = "Password must be at least 15 characters long")
        String password,
        @NotBlank(message = "First name is required")
        @Size(min = 3, message = "First name must be at least 3 characters long")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Size(min = 3, message = "Last name must be at least 3 characters long")
        String lastName) {
}
