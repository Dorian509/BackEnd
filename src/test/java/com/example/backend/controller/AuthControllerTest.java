package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.model.entity.UserProfile;
import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import com.example.backend.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests für AuthController.
 * Testet Registrierung, Login und Authentifizierung.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-15
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserProfileRepository userProfileRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void register_withValidData_shouldReturn201Created() throws Exception {
        // Gegeben - Gültige Registrierungsdaten
        RegisterRequest request = new RegisterRequest(
                "Max Mustermann",
                "max@example.com",
                "securePassword123",
                75,
                ActivityLevel.MEDIUM,
                Climate.NORMAL,
                "Europe/Berlin"
        );

        UserProfile savedProfile = new UserProfile();
        savedProfile.setId(1L);
        savedProfile.setName("Max Mustermann");
        savedProfile.setEmail("max@example.com");
        savedProfile.setPassword("hashedPassword");
        savedProfile.setWeightKg(75);
        savedProfile.setActivityLevel(ActivityLevel.MEDIUM);
        savedProfile.setClimate(Climate.NORMAL);
        savedProfile.setTimezone("Europe/Berlin");

        when(userProfileRepository.existsByEmail("max@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(savedProfile);

        // Wenn & Dann - Erfolgreiche Registrierung erwartet
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.email").value("max@example.com"))
                .andExpect(jsonPath("$.user.name").value("Max Mustermann"));

        verify(userProfileRepository).existsByEmail("max@example.com");
        verify(passwordEncoder).encode("securePassword123");
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void register_withExistingEmail_shouldReturn409Conflict() throws Exception {
        // Gegeben - Email existiert bereits
        RegisterRequest request = new RegisterRequest(
                "Max Mustermann",
                "existing@example.com",
                "securePassword123",
                75,
                ActivityLevel.MEDIUM,
                Climate.NORMAL,
                "Europe/Berlin"
        );

        when(userProfileRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Wenn & Dann - Conflict Error erwartet
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already exists"));

        verify(userProfileRepository).existsByEmail("existing@example.com");
        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    void login_withValidCredentials_shouldReturn200Ok() throws Exception {
        // Gegeben - Gültige Login-Daten
        LoginRequest request = new LoginRequest("max@example.com", "securePassword123");

        UserProfile existingProfile = new UserProfile();
        existingProfile.setId(1L);
        existingProfile.setName("Max Mustermann");
        existingProfile.setEmail("max@example.com");
        existingProfile.setPassword("hashedPassword");
        existingProfile.setWeightKg(75);
        existingProfile.setActivityLevel(ActivityLevel.MEDIUM);
        existingProfile.setClimate(Climate.NORMAL);

        when(userProfileRepository.findByEmail("max@example.com")).thenReturn(Optional.of(existingProfile));
        when(passwordEncoder.matches("securePassword123", "hashedPassword")).thenReturn(true);

        // Wenn & Dann - Erfolgreicher Login erwartet
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.email").value("max@example.com"))
                .andExpect(jsonPath("$.user.name").value("Max Mustermann"));

        verify(userProfileRepository).findByEmail("max@example.com");
        verify(passwordEncoder).matches("securePassword123", "hashedPassword");
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401Unauthorized() throws Exception {
        // Gegeben - Falsches Passwort
        LoginRequest request = new LoginRequest("max@example.com", "wrongPassword");

        UserProfile existingProfile = new UserProfile();
        existingProfile.setId(1L);
        existingProfile.setEmail("max@example.com");
        existingProfile.setPassword("hashedPassword");

        when(userProfileRepository.findByEmail("max@example.com")).thenReturn(Optional.of(existingProfile));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Wenn & Dann - Unauthorized Error erwartet
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));

        verify(userProfileRepository).findByEmail("max@example.com");
        verify(passwordEncoder).matches("wrongPassword", "hashedPassword");
    }

    @Test
    void login_withNonExistentEmail_shouldReturn401Unauthorized() throws Exception {
        // Gegeben - Nicht existierende Email
        LoginRequest request = new LoginRequest("nonexistent@example.com", "password123");

        when(userProfileRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Wenn & Dann - Unauthorized Error erwartet
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid email or password"));

        verify(userProfileRepository).findByEmail("nonexistent@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void getProfile_withValidUserId_shouldReturn200Ok() throws Exception {
        // Gegeben - Gültige Benutzer-ID
        UserProfile existingProfile = new UserProfile();
        existingProfile.setId(1L);
        existingProfile.setName("Max Mustermann");
        existingProfile.setEmail("max@example.com");
        existingProfile.setPassword("hashedPassword");

        when(userProfileRepository.findById(1L)).thenReturn(Optional.of(existingProfile));

        // Wenn & Dann - Profil erfolgreich abgerufen
        mockMvc.perform(get("/api/auth/profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.email").value("max@example.com"))
                .andExpect(jsonPath("$.user.name").value("Max Mustermann"))
                .andExpect(jsonPath("$.token").doesNotExist());

        verify(userProfileRepository).findById(1L);
    }

    @Test
    void getProfile_withNonExistentUserId_shouldReturn404NotFound() throws Exception {
        // Gegeben - Nicht existierende Benutzer-ID
        when(userProfileRepository.findById(999L)).thenReturn(Optional.empty());

        // Wenn & Dann - Not Found Error erwartet
        mockMvc.perform(get("/api/auth/profile/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));

        verify(userProfileRepository).findById(999L);
    }
}
