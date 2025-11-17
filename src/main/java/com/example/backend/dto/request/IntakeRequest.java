package com.example.backend.dto.request;

import com.example.backend.model.enums.IntakeSource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request-DTO zum Erfassen eines Wasseraufnahme-Events.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntakeRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Volume is required")
    @Min(value = 1, message = "Volume must be at least 1ml")
    private Integer volumeMl;

    @NotNull(message = "Source is required")
    private IntakeSource source;
}
