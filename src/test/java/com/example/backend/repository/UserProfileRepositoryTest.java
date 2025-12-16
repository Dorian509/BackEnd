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
}
