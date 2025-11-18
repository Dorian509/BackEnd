package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.ProfileRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.dto.response.ProfileResponse;
import com.example.backend.service.HydrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-Controller für Authentifizierungs-Operationen.
 * Verarbeitet Registrierung und Login von Benutzern.
 */
@CrossOrigin(
    origins = {
        "https://frontend-b5ow.onrender.com",
        "http://localhost:5173",
        "http://localhost:3000"
    },
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final HydrationService hydrationService;

    /**
     * Registriert einen neuen Benutzer.
     * Erstellt ein neues Benutzerprofil mit den angegebenen Hydrationspräferenzen.
     *
     * @param request Registrierungsdaten
     * @return Erstelltes Benutzerprofil mit ID
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/auth/register - Registering new user");

        // Konvertiere RegisterRequest zu ProfileRequest
        ProfileRequest profileRequest = new ProfileRequest(
            request.getWeightKg(),
            request.getActivityLevel(),
            request.getClimate(),
            request.getTimezone()
        );

        // Erstelle Profil über HydrationService
        ProfileResponse profile = hydrationService.createProfile(profileRequest);

        // Erstelle AuthResponse
        AuthResponse response = new AuthResponse(
            profile.getId(),
            profile.getWeightKg(),
            profile.getActivityLevel(),
            profile.getClimate(),
            profile.getTimezone(),
            "Registration successful"
        );

        log.info("User registered with ID {}", profile.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Meldet einen Benutzer an.
     * Gibt das Benutzerprofil für die angegebene ID zurück.
     *
     * @param request Login-Daten mit Benutzer-ID
     * @return Benutzerprofil
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - User {} logging in", request.getUserId());

        // Hole Profil über HydrationService
        ProfileResponse profile = hydrationService.getProfile(request.getUserId());

        // Erstelle AuthResponse
        AuthResponse response = new AuthResponse(
            profile.getId(),
            profile.getWeightKg(),
            profile.getActivityLevel(),
            profile.getClimate(),
            profile.getTimezone(),
            "Login successful"
        );

        log.info("User {} logged in successfully", request.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * Holt das Profil des aktuellen Benutzers.
     *
     * @param userId Benutzer-ID
     * @return Benutzerprofil
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<AuthResponse> getProfile(@PathVariable Long userId) {
        log.info("GET /api/auth/profile/{} - Getting user profile", userId);

        ProfileResponse profile = hydrationService.getProfile(userId);

        AuthResponse response = new AuthResponse(
            profile.getId(),
            profile.getWeightKg(),
            profile.getActivityLevel(),
            profile.getClimate(),
            profile.getTimezone(),
            "Profile retrieved"
        );

        return ResponseEntity.ok(response);
    }
}
