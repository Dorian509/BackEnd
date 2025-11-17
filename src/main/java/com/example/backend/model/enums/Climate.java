package com.example.backend.model.enums;

/**
 * Klimabedingungen, die den Hydratationsbedarf beeinflussen.
 * Heiße Klimazonen erfordern eine erhöhte Wasseraufnahme.
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
