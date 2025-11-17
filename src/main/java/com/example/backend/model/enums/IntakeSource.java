package com.example.backend.model.enums;

/**
 * Quelltyp für die Wasseraufnahme-Verfolgung.
 * Repräsentiert verschiedene Trinkmethoden/-mengen.
 */
public enum IntakeSource {
    /**
     * Kleiner Schluck - typischerweise ~100ml
     */
    SIP,

    /**
     * Doppelter Schluck - typischerweise ~200ml
     */
    DOUBLE_SIP,

    /**
     * Volles Glas - typischerweise ~250ml
     */
    GLASS
}
