package com.example.backend.dto.response;

import com.example.backend.model.enums.IntakeSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response-DTO für Aufnahme-Event.
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
