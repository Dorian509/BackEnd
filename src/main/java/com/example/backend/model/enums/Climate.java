package com.example.backend.model.enums;

/**
 * Klimabedingungen, die den Hydratationsbedarf beeinflussen.
 * Heiße Klimazonen erfordern eine erhöhte Wasseraufnahme.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-13
 */
public enum Climate {
    /**
     * Normale Klimabedingungen
     * Keine zusätzliche Wasseraufnahme erforderlich
     */
    NORMAL,

    /**
     * Heiße Klimabedingungen
     * Fügt 500ml zum Tagesziel hinzu
     */
    HOT
}
