package com.example.demo.repository;

import com.example.demo.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository für UserProfile-Entity.
 * Bietet CRUD-Operationen und benutzerdefinierte Abfragen für Benutzerprofile.
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // Zusätzliche benutzerdefinierte Abfragen können hier bei Bedarf hinzugefügt werden
}
