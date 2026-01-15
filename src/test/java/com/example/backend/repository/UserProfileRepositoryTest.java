package com.example.backend.repository;

import com.example.backend.model.entity.UserProfile;
import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository Tests für UserProfileRepository mit H2 In-Memory Datenbank.
 * Testet Custom Query-Methode findByEmail.
 */
@DataJpaTest
class UserProfileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserProfile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new UserProfile();
        testProfile.setName("Test User");
        testProfile.setEmail("test@example.com");
        testProfile.setPassword("password123");
        testProfile.setWeightKg(70);
        testProfile.setActivityLevel(ActivityLevel.MEDIUM);
        testProfile.setClimate(Climate.NORMAL);
        testProfile.setTimezone("Europe/Berlin");

        entityManager.persistAndFlush(testProfile);
    }

    @Test
    void findByEmail_withExistingEmail_shouldReturnUser() {
        // When
        Optional<UserProfile> found = userProfileRepository.findByEmail("test@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getName()).isEqualTo("Test User");
        assertThat(found.get().getWeightKg()).isEqualTo(70);
    }

    @Test
    void findByEmail_withNonExistentEmail_shouldReturnEmpty() {
        // Wenn - Suche nach nicht existierender Email
        Optional<UserProfile> found = userProfileRepository.findByEmail("nonexistent@example.com");

        // Dann - Leeres Optional erwartet
        assertThat(found).isEmpty();
    }

    @Test
    void existsByEmail_withExistingEmail_shouldReturnTrue() {
        // Wenn - Prüfung ob existierende Email vorhanden ist
        boolean exists = userProfileRepository.existsByEmail("test@example.com");

        // Dann - True erwartet
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_withNonExistentEmail_shouldReturnFalse() {
        // Wenn - Prüfung ob nicht existierende Email vorhanden ist
        boolean exists = userProfileRepository.existsByEmail("nonexistent@example.com");

        // Dann - False erwartet
        assertThat(exists).isFalse();
    }

    @Test
    void save_shouldPersistUserProfileWithAllFields() {
        // Gegeben - Neues Benutzerprofil mit allen Feldern
        UserProfile newProfile = new UserProfile();
        newProfile.setName("New User");
        newProfile.setEmail("new@example.com");
        newProfile.setPassword("hashedPassword");
        newProfile.setWeightKg(80);
        newProfile.setActivityLevel(ActivityLevel.HIGH);
        newProfile.setClimate(Climate.HOT);
        newProfile.setTimezone("America/New_York");

        // Wenn - Profil wird gespeichert
        UserProfile saved = userProfileRepository.save(newProfile);
        entityManager.flush();
        entityManager.clear();

        // Dann - Alle Felder korrekt persistiert
        Optional<UserProfile> found = userProfileRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("New User");
        assertThat(found.get().getEmail()).isEqualTo("new@example.com");
        assertThat(found.get().getPassword()).isEqualTo("hashedPassword");
        assertThat(found.get().getWeightKg()).isEqualTo(80);
        assertThat(found.get().getActivityLevel()).isEqualTo(ActivityLevel.HIGH);
        assertThat(found.get().getClimate()).isEqualTo(Climate.HOT);
        assertThat(found.get().getTimezone()).isEqualTo("America/New_York");
    }
}
