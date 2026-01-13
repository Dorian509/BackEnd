package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response-DTO für den heutigen Hydrationsstatus.
 * Enthält Ziel, konsumierte Menge und verbleibende Menge.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodayStatusResponse {

    /**
     * Tägliches Hydrationsziel in Millilitern
     */
    private int goalMl;

    /**
     * Heute konsumierte Menge in Millilitern
     */
    private int consumedMl;

    /**
     * Verbleibende Menge zum Erreichen des Ziels in Millilitern
     */
    private int remainingMl;

    /**
     * Prozentsatz des erreichten Ziels (0-100+)
     */
    private int percentageAchieved;
}
