package com.example.backend.model.entity;

import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
     * Name des Benutzers
     */
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    /**
     * E-Mail-Adresse des Benutzers (eindeutig)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Passwort des Benutzers
     */
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

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
