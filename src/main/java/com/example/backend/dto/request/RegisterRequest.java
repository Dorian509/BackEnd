package com.example.backend.dto.request;

import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request-DTO für die Benutzerregistrierung.
 * Erstellt einen neuen Benutzer mit Authentifizierungsdaten und Hydrationspräferenzen.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Weight is required")
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 200, message = "Weight must not exceed 200 kg")
    private Integer weightKg;

    @NotNull(message = "Activity level is required")
    private ActivityLevel activityLevel;

    @NotNull(message = "Climate is required")
    private Climate climate;

    private String timezone;
}
