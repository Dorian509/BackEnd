package com.example.backend.repository;

import com.example.backend.model.entity.IntakeEvent;
import com.example.backend.model.entity.UserProfile;
import com.example.backend.model.enums.ActivityLevel;
import com.example.backend.model.enums.Climate;
import com.example.backend.model.enums.IntakeSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository Tests für IntakeEventRepository mit H2 In-Memory Datenbank.
 * Testet Custom Queries.
 */
@DataJpaTest
class IntakeEventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IntakeEventRepository intakeEventRepository;

    private UserProfile testUser;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();

        testUser = new UserProfile();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setWeightKg(70);
        testUser.setActivityLevel(ActivityLevel.MEDIUM);
        testUser.setClimate(Climate.NORMAL);
        testUser.setTimezone("Europe/Berlin");
        entityManager.persistAndFlush(testUser);

        createIntakeEvent(testUser, 500, IntakeSource.GLASS, now);
        createIntakeEvent(testUser, 300, IntakeSource.DOUBLE_SIP, now);

        entityManager.flush();
    }

    private void createIntakeEvent(UserProfile user, int volumeMl, IntakeSource source, Instant timestamp) {
        IntakeEvent event = new IntakeEvent();
        event.setUser(user);
        event.setVolumeMl(volumeMl);
        event.setSource(source);
        event.setTimestampUtc(timestamp);
        entityManager.persist(event);
    }

    @Test
    void sumForUserBetween_shouldCalculateCorrectSum() {
        // Given: 2 Events (500ml + 300ml = 800ml)
        Instant start = now.minus(1, ChronoUnit.HOURS);
        Instant end = now.plus(1, ChronoUnit.HOURS);

        // When
        int totalMl = intakeEventRepository.sumForUserBetween(testUser.getId(), start, end);

        // Then
        assertThat(totalMl).isEqualTo(800);
    }

    @Test
    void findByUserAndTimestampBetween_shouldReturnEventsInRange() {
        // Given: 2 Events
        Instant start = now.minus(1, ChronoUnit.HOURS);
        Instant end = now.plus(1, ChronoUnit.HOURS);

        // When
        List<IntakeEvent> events = intakeEventRepository.findByUserAndTimestampBetween(
                testUser.getId(), start, end);

        // Then
        assertThat(events).hasSize(2);
        assertThat(events).extracting(IntakeEvent::getVolumeMl)
                .containsExactlyInAnyOrder(500, 300);
    }
}
