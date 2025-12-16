package com.example.backend.controller;

import com.example.backend.dto.request.IntakeRequest;
import com.example.backend.dto.request.ProfileRequest;
import com.example.backend.dto.response.IntakeResponse;
import com.example.backend.dto.response.ProfileResponse;
import com.example.backend.dto.response.TodayStatusResponse;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import com.example.backend.model.enums.IntakeSource;
import com.example.backend.service.HydrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests für HydrationController.
 * Testet die wichtigsten REST-Endpoints.
 */
@WebMvcTest(HydrationController.class)
class HydrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HydrationService hydrationService;

    @Test
    void createProfile_withValidData_shouldReturn201Created() throws Exception {
        // Given
        ProfileRequest request = new ProfileRequest(70, ActivityLevel.MEDIUM, Climate.NORMAL, "Europe/Berlin");
        ProfileResponse response = new ProfileResponse(1L, 70, ActivityLevel.MEDIUM, Climate.NORMAL, "Europe/Berlin");

        doReturn(response).when(hydrationService).createProfile(any(ProfileRequest.class));

        // When & Then
        mockMvc.perform(post("/api/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.weightKg").value(70))
                .andExpect(jsonPath("$.activityLevel").value("MEDIUM"));

        verify(hydrationService).createProfile(any(ProfileRequest.class));
    }

    @Test
    void getTodayStatus_withValidUserId_shouldReturn200Ok() throws Exception {
        // Given
        TodayStatusResponse response = new TodayStatusResponse(2700, 1500, 1200, 56);
        doReturn(response).when(hydrationService).getTodayStatus(1L);

        // When & Then
        mockMvc.perform(get("/api/hydration/today/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goalMl").value(2700))
                .andExpect(jsonPath("$.consumedMl").value(1500))
                .andExpect(jsonPath("$.remainingMl").value(1200))
                .andExpect(jsonPath("$.percentageAchieved").value(56));

        verify(hydrationService).getTodayStatus(1L);
    }

    @Test
    void addIntake_withValidData_shouldReturn201Created() throws Exception {
        // Given
        IntakeRequest request = new IntakeRequest(1L, 250, IntakeSource.SIP);
        IntakeResponse response = new IntakeResponse(1L, 1L, 250, IntakeSource.SIP, Instant.now());

        doReturn(response).when(hydrationService).recordIntake(any(IntakeRequest.class));

        // When & Then
        mockMvc.perform(post("/api/intakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.volumeMl").value(250))
                .andExpect(jsonPath("$.source").value("SIP"));

        verify(hydrationService).recordIntake(any(IntakeRequest.class));
    }
}
