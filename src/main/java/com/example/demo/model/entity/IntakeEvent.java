package com.example.demo.model.entity;

import com.example.demo.model.enums.IntakeSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Aufnahme-Event-Entity, die einen Wasserkonsum-Datensatz repräsentiert.
 * Verfolgt wann und wie viel Wasser ein Benutzer konsumiert hat.
 */
@Entity
@Table(name = "intake_event", indexes = {
    @Index(name = "idx_user_timestamp", columnList = "user_id,timestamp_utc")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntakeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referenz zum Benutzer, der Wasser konsumiert hat
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private UserProfile user;

    /**
     * Menge des konsumierten Wassers in Millilitern
     * Muss mindestens 1ml sein
     */
    @NotNull(message = "Volume is required")
    @Min(value = 1, message = "Volume must be at least 1ml")
    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    /**
     * Quelle/Methode des Wasserkonsums
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false)
    private IntakeSource source = IntakeSource.SIP;

    /**
     * Zeitstempel, wann das Wasser konsumiert wurde (UTC)
     */
    @Column(name = "timestamp_utc", nullable = false)
    private Instant timestampUtc = Instant.now();

    /**
     * Pre-Persist-Callback zum Sicherstellen, dass der Zeitstempel gesetzt ist
     */
    @PrePersist
    protected void onCreate() {
        if (timestampUtc == null) {
            timestampUtc = Instant.now();
        }
    }
}
