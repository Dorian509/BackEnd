package com.example.backend.repository;

import com.example.backend.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository für UserProfile-Entity.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Benutzerprofile.
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Findet einen Benutzer anhand seiner E-Mail-Adresse.
     *
     * @param email E-Mail-Adresse
     * @return Optional mit UserProfile falls gefunden
     */
    java.util.Optional<UserProfile> findByEmail(String email);

    /**
     * Prüft ob eine E-Mail bereits existiert.
     *
     * @param email E-Mail-Adresse
     * @return true wenn E-Mail existiert
     */
    boolean existsByEmail(String email);
}
