package com.example.backend.dto.response;

import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response-DTO für das Benutzerprofil.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long id;
    private Integer weightKg;
    private ActivityLevel activityLevel;
    private Climate climate;
    private String timezone;
}
