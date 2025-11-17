package com.example.backend.service;

import com.example.backend.dto.request.IntakeRequest;
import com.example.backend.dto.request.ProfileRequest;
import com.example.backend.dto.response.IntakeResponse;
import com.example.backend.dto.response.ProfileResponse;
import com.example.backend.dto.response.TodayStatusResponse;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.model.entity.IntakeEvent;
import com.example.backend.model.entity.UserProfile;
import com.example.backend.repository.IntakeEventRepository;
import com.example.backend.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service für hydrationsbezogene Geschäftslogik.
 * Verarbeitet Berechnungen, Datenpersistierung und Geschäftsregeln.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HydrationService {

    private final UserProfileRepository profileRepository;
    private final IntakeEventRepository intakeRepository;

    /**
     * Berechnet das tägliche Hydrationsziel basierend auf dem Benutzerprofil.
     *
     * @param profile Benutzerprofil
     * @return Tagesziel in Millilitern
     */
    public int calculateDailyGoalMl(UserProfile profile) {
        // Basisberechnung: 35ml pro kg Körpergewicht
        int baseGoal = profile.getWeightKg() * 35;

        // Aktivitätslevel-Bonus
        int activityBonus = switch (profile.getActivityLevel()) {
            case HIGH -> 500;
            case MEDIUM -> 250;
            case LOW -> 0;
        };

        // Klima-Bonus
        int climateBonus = profile.getClimate() == com.example.backend.model.enums.Climate.HOT ? 500 : 0;

        // Runden auf nächste 50ml
        int totalGoal = baseGoal + activityBonus + climateBonus;
        return Math.round(totalGoal / 50.0f) * 50;
    }

    /**
     * Holt den heutigen Hydrationsstatus für einen Benutzer.
     *
     * @param userId Benutzer-ID
     * @return Heutiger Status mit Ziel, konsumierter und verbleibender Menge
     */
    @Transactional(readOnly = true)
    public TodayStatusResponse getTodayStatus(Long userId) {
        log.debug("Getting today's status for user {}", userId);

        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));

        ZoneId zoneId = ZoneId.of(profile.getTimezone());
        Instant startOfDay = LocalDate.now(zoneId).atStartOfDay(zoneId).toInstant();
        Instant endOfDay = LocalDate.now(zoneId).plusDays(1).atStartOfDay(zoneId).toInstant();

        int goalMl = calculateDailyGoalMl(profile);
        int consumedMl = intakeRepository.sumForUserBetween(userId, startOfDay, endOfDay);
        int remainingMl = Math.max(0, goalMl - consumedMl);
        int percentageAchieved = (int) Math.round((consumedMl * 100.0) / goalMl);

        log.info("User {} status: {}/{} ml ({}%)", userId, consumedMl, goalMl, percentageAchieved);

        return new TodayStatusResponse(goalMl, consumedMl, remainingMl, percentageAchieved);
    }

    /**
     * Erstellt ein neues Benutzerprofil.
     *
     * @param request Profil-Anfragedaten
     * @return Erstelltes Profil
     */
    @Transactional
    public ProfileResponse createProfile(ProfileRequest request) {
        log.info("Creating new profile");

        UserProfile profile = new UserProfile();
        profile.setWeightKg(request.getWeightKg());
        profile.setActivityLevel(request.getActivityLevel());
        profile.setClimate(request.getClimate());
        profile.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Europe/Berlin");

        UserProfile saved = profileRepository.save(profile);
        log.info("Profile created with ID {}", saved.getId());

        return mapToProfileResponse(saved);
    }

    /**
     * Aktualisiert ein bestehendes Benutzerprofil.
     *
     * @param userId  Benutzer-ID
     * @param request Aktualisierte Profildaten
     * @return Aktualisiertes Profil
     */
    @Transactional
    public ProfileResponse updateProfile(Long userId, ProfileRequest request) {
        log.info("Updating profile for user {}", userId);

        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));

        profile.setWeightKg(request.getWeightKg());
        profile.setActivityLevel(request.getActivityLevel());
        profile.setClimate(request.getClimate());
        if (request.getTimezone() != null) {
            profile.setTimezone(request.getTimezone());
        }

        UserProfile saved = profileRepository.save(profile);
        log.info("Profile updated for user {}", userId);

        return mapToProfileResponse(saved);
    }

    /**
     * Holt ein Benutzerprofil anhand der ID.
     *
     * @param userId Benutzer-ID
     * @return Benutzerprofil
     */
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {
        log.debug("Getting profile for user {}", userId);

        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));

        return mapToProfileResponse(profile);
    }

    /**
     * Erfasst ein neues Wasseraufnahme-Event.
     *
     * @param request Aufnahme-Anfragedaten
     * @return Erstelltes Aufnahme-Event
     */
    @Transactional
    public IntakeResponse recordIntake(IntakeRequest request) {
        log.info("Recording intake for user {}: {}ml from {}",
                request.getUserId(), request.getVolumeMl(), request.getSource());

        UserProfile user = profileRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", request.getUserId()));

        IntakeEvent event = new IntakeEvent();
        event.setUser(user);
        event.setVolumeMl(request.getVolumeMl());
        event.setSource(request.getSource());
        event.setTimestampUtc(Instant.now());

        IntakeEvent saved = intakeRepository.save(event);
        log.info("Intake recorded with ID {}", saved.getId());

        return mapToIntakeResponse(saved);
    }

    /**
     * Holt die letzten Aufnahme-Events für einen Benutzer.
     *
     * @param userId Benutzer-ID
     * @param limit  Maximale Anzahl der Ergebnisse
     * @return Liste der letzten Aufnahme-Events
     */
    @Transactional(readOnly = true)
    public List<IntakeResponse> getRecentIntakes(Long userId, int limit) {
        log.debug("Getting {} recent intakes for user {}", limit, userId);

        List<IntakeEvent> events = intakeRepository.findRecentByUser(userId, limit);
        return events.stream()
                .map(this::mapToIntakeResponse)
                .collect(Collectors.toList());
    }

    /**
     * Löscht ein Aufnahme-Event.
     *
     * @param intakeId Aufnahme-Event-ID
     */
    @Transactional
    public void deleteIntake(Long intakeId) {
        log.info("Deleting intake {}", intakeId);

        if (!intakeRepository.existsById(intakeId)) {
            throw new ResourceNotFoundException("IntakeEvent", intakeId);
        }

        intakeRepository.deleteById(intakeId);
        log.info("Intake {} deleted", intakeId);
    }

    // Mapping-Methoden

    private ProfileResponse mapToProfileResponse(UserProfile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getWeightKg(),
                profile.getActivityLevel(),
                profile.getClimate(),
                profile.getTimezone()
        );
    }

    private IntakeResponse mapToIntakeResponse(IntakeEvent event) {
        return new IntakeResponse(
                event.getId(),
                event.getUser().getId(),
                event.getVolumeMl(),
                event.getSource(),
                event.getTimestampUtc()
        );
    }
}
