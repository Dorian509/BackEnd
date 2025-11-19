package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response-DTO für Authentifizierungs-Operationen.
 * Enthält Token und User-Daten für Frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private UserInfo user;
    private String error;

    // Nested class for user info
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String email;
        private String name;
    }

    // Constructor for success response
    public AuthResponse(String token, Long userId, String email, String name) {
        this.token = token;
        this.user = new UserInfo(userId, email, name);
        this.error = null;
    }

    // Constructor for error response
    public static AuthResponse error(String errorMessage) {
        AuthResponse response = new AuthResponse();
        response.setError(errorMessage);
        return response;
    }
}