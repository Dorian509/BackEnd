package com.example.demo.dto.request;

import com.example.demo.model.enums.ActivityLevel;
import com.example.demo.model.enums.Climate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request-DTO zum Erstellen oder Aktualisieren eines Benutzerprofils.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

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
