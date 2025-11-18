package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health-Check-Controller.
 * Bietet Endpunkte zur Überwachung des Anwendungsstatus.
 */
@CrossOrigin(
    origins = {
        "https://frontend-b5ow.onrender.com",
        "http://localhost:5173",
        "http://localhost:3000"
    },
    methods = {RequestMethod.GET, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true"
)
@RestController
@RequestMapping
public class HealthController {

    /**
     * Root-Endpunkt - Basis-Health-Check
     *
     * @return Statusnachricht
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of(
                "message", "HydrateMate API is running",
                "status", "ok",
                "timestamp", Instant.now().toString()
        ));
    }

    /**
     * Health-Check-Endpunkt
     *
     * @return Gesundheitsstatus
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now(),
                "service", "HydrateMate Backend",
                "version", "1.0.0"
        ));
    }
}
