package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request-DTO für den Benutzer-Login.
 * Verwendet die Benutzer-ID zur Identifikation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "User ID is required")
    private Long userId;
}
