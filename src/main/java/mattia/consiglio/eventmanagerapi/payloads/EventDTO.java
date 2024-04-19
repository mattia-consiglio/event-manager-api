package mattia.consiglio.eventmanagerapi.payloads;

import jakarta.validation.constraints.*;

public record EventDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,
        @NotBlank(message = "Description is required")
        @Size(min = 3, message = "Description must be at least 3 characters long")
        String description,
        @NotBlank(message = "Location is required")
        @Size(min = 3, message = "Location must be at least 3 characters long")
        String location,
        @NotNull(message = "Date is required")
        @FutureOrPresent
        String date,
        @NotNull(message = "Available tickets is required")
        @Min(1)
        @Positive
        int availableTickets
) {
}
