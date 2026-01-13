package com.example.backend.dto.response;

import com.example.backend.model.enums.IntakeSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response-DTO für Aufnahme-Event.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntakeResponse {

    private Long id;
    private Long userId;
    private Integer volumeMl;
    private IntakeSource source;
    private Instant timestamp;
}
