package com.example.demo.model.entity;

import com.example.demo.model.enums.ActivityLevel;
import com.example.demo.model.enums.Climate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Benutzerprofil-Entity mit persönlichen Hydrationspräferenzen.
 * Speichert Gewicht, Aktivitätslevel, Klima und Zeitzone für personalisierte Hydrationsziele.
 */
@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Gewicht des Benutzers in Kilogramm (20-200 kg)
     * Wird zur Berechnung des täglichen Hydratationsziels verwendet (Gewicht * 35ml)
     */
    @NotNull(message = "Weight is required")
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 200, message = "Weight must not exceed 200 kg")
    private Integer weightKg;

    /**
     * Aktivitätslevel des Benutzers
     * Beeinflusst die Berechnung des täglichen Hydratationsziels
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", nullable = false)
    private ActivityLevel activityLevel = ActivityLevel.MEDIUM;

    /**
     * Klimabedingungen des Benutzers
     * Heiße Klimazonen erhöhen den Hydratationsbedarf
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "climate", nullable = false)
    private Climate climate = Climate.NORMAL;

    /**
     * Zeitzone des Benutzers für präzise Tagesgrenzen
     * Standardwert ist Europe/Berlin
     */
    @Column(name = "timezone", nullable = false)
    private String timezone = "Europe/Berlin";
}
