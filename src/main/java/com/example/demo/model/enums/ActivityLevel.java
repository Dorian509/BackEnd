package com.example.demo.model.enums;

/**
 * Aktivitätslevel-Enum zur Berechnung des täglichen Hydratationsziels.
 * Höhere Aktivitätslevel erhöhen die empfohlene tägliche Wasseraufnahme.
 */
public enum ActivityLevel {
    /**
     * Niedriges Aktivitätslevel - minimale körperliche Aktivität
     * Keine zusätzliche Wasseraufnahme erforderlich
     */
    LOW,

    /**
     * Mittleres Aktivitätslevel - moderate körperliche Aktivität
     * Fügt 250ml zum Tagesziel hinzu
     */
    MEDIUM,

    /**
     * Hohes Aktivitätslevel - intensive körperliche Aktivität
     * Fügt 500ml zum Tagesziel hinzu
     */
    HIGH
}
