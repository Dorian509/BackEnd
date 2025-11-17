package com.example.backend.repository;

import com.example.backend.model.entity.IntakeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository für IntakeEvent-Entity.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Aufnahme-Events.
 */
@Repository
public interface IntakeEventRepository extends JpaRepository<IntakeEvent, Long> {

    /**
     * Berechnet die gesamte Wasseraufnahme für einen Benutzer innerhalb eines Zeitraums.
     *
     * @param userId Benutzer-ID
     * @param start  Startzeit (inklusiv)
     * @param end    Endzeit (exklusiv)
     * @return Gesamtvolumen in Millilitern
     */
    @Query("""
        SELECT COALESCE(SUM(e.volumeMl), 0)
        FROM IntakeEvent e
        WHERE e.user.id = :userId
          AND e.timestampUtc >= :start
          AND e.timestampUtc < :end
        """)
    int sumForUserBetween(@Param("userId") Long userId,
                          @Param("start") Instant start,
                          @Param("end") Instant end);

    /**
     * Findet alle Aufnahme-Events für einen Benutzer innerhalb eines Zeitraums.
     *
     * @param userId Benutzer-ID
     * @param start  Startzeit (inklusiv)
     * @param end    Endzeit (exklusiv)
     * @return Liste der Aufnahme-Events
     */
    @Query("""
        SELECT e
        FROM IntakeEvent e
        WHERE e.user.id = :userId
          AND e.timestampUtc >= :start
          AND e.timestampUtc < :end
        ORDER BY e.timestampUtc DESC
        """)
    List<IntakeEvent> findByUserAndTimestampBetween(@Param("userId") Long userId,
                                                     @Param("start") Instant start,
                                                     @Param("end") Instant end);

    /**
     * Findet die letzten Aufnahme-Events für einen Benutzer.
     *
     * @param userId Benutzer-ID
     * @param limit  Maximale Anzahl der Ergebnisse
     * @return Liste der letzten Aufnahme-Events
     */
    @Query("""
        SELECT e
        FROM IntakeEvent e
        WHERE e.user.id = :userId
        ORDER BY e.timestampUtc DESC
        LIMIT :limit
        """)
    List<IntakeEvent> findRecentByUser(@Param("userId") Long userId,
                                        @Param("limit") int limit);
}
