package com.example.backend.dto.response;

import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response-DTO für Authentifizierungs-Operationen.
 * Enthält Benutzer-ID und Profildaten.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private Integer weightKg;
    private ActivityLevel activityLevel;
    private Climate climate;
    private String timezone;
    private String message;

    public AuthResponse(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
