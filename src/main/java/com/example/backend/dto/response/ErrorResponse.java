package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Standard-Fehlerantwort-DTO.
 * Wird für alle Fehlerantworten in der API verwendet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Zeitstempel, wann der Fehler aufgetreten ist
     */
    private Instant timestamp;

    /**
     * HTTP-Statuscode
     */
    private int status;

    /**
     * Fehlertyp/-kategorie
     */
    private String error;

    /**
     * Detaillierte Fehlermeldung
     */
    private String message;

    /**
     * Anfragepfad, wo der Fehler aufgetreten ist
     */
    private String path;

    /**
     * Konstruktor zum Erstellen einer Fehlerantwort mit aktuellem Zeitstempel
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
