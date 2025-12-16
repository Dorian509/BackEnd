package com.example.backend.service;

import com.example.backend.dto.request.IntakeRequest;
import com.example.backend.dto.response.TodayStatusResponse;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.entity.IntakeEvent;
import com.example.backend.model.entity.UserProfile;
import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import com.example.backend.model.enums.IntakeSource;
import com.example.backend.repository.IntakeEventRepository;
import com.example.backend.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit Tests für HydrationService mit gemockten Repositories.
 * Testet die wichtigsten Business-Logik-Methoden.
 */
@ExtendWith(MockitoExtension.class)
class HydrationServiceTest {

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private IntakeEventRepository intakeRepository;

    @InjectMocks
    private HydrationService hydrationService;

    private UserProfile testProfile;
    private IntakeEvent testIntake;

    @BeforeEach
    void setUp() {
        testProfile = new UserProfile();
        testProfile.setId(1L);
        testProfile.setName("Test User");
        testProfile.setEmail("test@example.com");
        testProfile.setPassword("password123");
        testProfile.setWeightKg(70);
        testProfile.setActivityLevel(ActivityLevel.MEDIUM);
        testProfile.setClimate(Climate.NORMAL);
        testProfile.setTimezone("Europe/Berlin");

        testIntake = new IntakeEvent();
        testIntake.setId(1L);
        testIntake.setUser(testProfile);
        testIntake.setVolumeMl(250);
        testIntake.setSource(IntakeSource.SIP);
        testIntake.setTimestampUtc(Instant.now());
    }

    @Test
    void calculateDailyGoalMl_withMediumActivityNormalClimate_shouldReturnCorrectGoal() {
        // Given: 70kg, MEDIUM activity, NORMAL climate
        // Base: 70 * 35 = 2450ml + Activity: 250ml + Climate: 0ml = 2700ml

        // When
        int goalMl = hydrationService.calculateDailyGoalMl(testProfile);

        // Then
        assertThat(goalMl).isEqualTo(2700);
    }

    @Test
    void getTodayStatus_withValidUser_shouldReturnCorrectStatus() {
        // Given
        doReturn(Optional.of(testProfile)).when(profileRepository).findById(1L);
        doReturn(1500).when(intakeRepository).sumForUserBetween(anyLong(), any(Instant.class), any(Instant.class));

        // When
        TodayStatusResponse status = hydrationService.getTodayStatus(1L);

        // Then
        assertThat(status).isNotNull();
        assertThat(status.getGoalMl()).isEqualTo(2700);
        assertThat(status.getConsumedMl()).isEqualTo(1500);
        assertThat(status.getRemainingMl()).isEqualTo(1200);
        assertThat(status.getPercentageAchieved()).isEqualTo(56);

        verify(profileRepository).findById(1L);
        verify(intakeRepository).sumForUserBetween(anyLong(), any(Instant.class), any(Instant.class));
    }

    @Test
    void recordIntake_withValidRequest_shouldCreateAndReturnIntake() {
        // Given
        IntakeRequest request = new IntakeRequest(1L, 250, IntakeSource.SIP);
        doReturn(Optional.of(testProfile)).when(profileRepository).findById(1L);
        doReturn(testIntake).when(intakeRepository).save(any(IntakeEvent.class));

        // When
        hydrationService.recordIntake(request);

        // Then
        verify(profileRepository).findById(1L);
        verify(intakeRepository).save(any(IntakeEvent.class));
    }

    @Test
    void deleteIntake_withNonExistentIntake_shouldThrowException() {
        // Given
        doReturn(false).when(intakeRepository).existsById(999L);

        // When & Then
        assertThatThrownBy(() -> hydrationService.deleteIntake(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(intakeRepository).existsById(999L);
        verify(intakeRepository, never()).deleteById(anyLong());
    }
}
