package com.example.backend.controller;

import com.example.backend.dto.request.IntakeRequest;
import com.example.backend.dto.request.ProfileRequest;
import com.example.backend.dto.response.IntakeResponse;
import com.example.backend.dto.response.ProfileResponse;
import com.example.backend.dto.response.TodayStatusResponse;
import com.example.backend.service.HydrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Hydrationsverfolgung.
 * Verarbeitet alle API-Endpunkte für Profile, Aufnahmen und Hydrationsstatus.
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HydrationController {

    private final HydrationService hydrationService;

    // ==================== Profil-Endpunkte ====================

    /**
     * Erstellt ein neues Benutzerprofil
     *
     * @param request Profildaten
     * @return Erstelltes Profil
     */
    @PostMapping("/profile")
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody ProfileRequest request) {
        log.info("POST /api/profile - Creating new profile");
        ProfileResponse response = hydrationService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Holt Benutzerprofil anhand der ID
     *
     * @param id Benutzer-ID
     * @return Benutzerprofil
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long id) {
        log.info("GET /api/profile/{} - Getting profile", id);
        ProfileResponse response = hydrationService.getProfile(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Aktualisiert ein Benutzerprofil
     *
     * @param id      Benutzer-ID
     * @param request Aktualisierte Profildaten
     * @return Aktualisiertes Profil
     */
    @PutMapping("/profile/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProfileRequest request) {
        log.info("PUT /api/profile/{} - Updating profile", id);
        ProfileResponse response = hydrationService.updateProfile(id, request);
        return ResponseEntity.ok(response);
    }

    // ==================== Hydrationsstatus-Endpunkte ====================

    /**
     * Holt den heutigen Hydrationsstatus für einen Benutzer
     *
     * @param userId Benutzer-ID
     * @return Heutiger Status (Ziel, konsumiert, verbleibend)
     */
    @GetMapping("/hydration/today/{userId}")
    public ResponseEntity<TodayStatusResponse> getTodayStatus(@PathVariable Long userId) {
        log.info("GET /api/hydration/today/{} - Getting today's status", userId);
        TodayStatusResponse response = hydrationService.getTodayStatus(userId);
        return ResponseEntity.ok(response);
    }

    // ==================== Aufnahme-Endpunkte ====================

    /**
     * Erfasst ein neues Wasseraufnahme-Event
     *
     * @param request Aufnahmedaten
     * @return Erstelltes Aufnahme-Event
     */
    @PostMapping("/intakes")
    public ResponseEntity<IntakeResponse> addIntake(@Valid @RequestBody IntakeRequest request) {
        log.info("POST /api/intakes - Recording intake for user {}", request.getUserId());
        IntakeResponse response = hydrationService.recordIntake(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Holt die letzten Aufnahme-Events für einen Benutzer
     *
     * @param userId Benutzer-ID
     * @param limit  Maximale Anzahl der Ergebnisse (Standard: 10)
     * @return Liste der letzten Aufnahme-Events
     */
    @GetMapping("/intakes/{userId}/recent")
    public ResponseEntity<List<IntakeResponse>> getRecentIntakes(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/intakes/{}/recent?limit={} - Getting recent intakes", userId, limit);
        List<IntakeResponse> response = hydrationService.getRecentIntakes(userId, limit);
        return ResponseEntity.ok(response);
    }

    /**
     * Löscht ein Aufnahme-Event
     *
     * @param intakeId Aufnahme-Event-ID
     * @return Kein Inhalt
     */
    @DeleteMapping("/intakes/{intakeId}")
    public ResponseEntity<Void> deleteIntake(@PathVariable Long intakeId) {
        log.info("DELETE /api/intakes/{} - Deleting intake", intakeId);
        hydrationService.deleteIntake(intakeId);
        return ResponseEntity.noContent().build();
    }
}
