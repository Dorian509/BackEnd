package com.example.backend.controller;

import com.example.backend.dto.request.LoginRequest;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.dto.response.AuthResponse;
import com.example.backend.model.entity.UserProfile;
import com.example.backend.repository.UserProfileRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

/**
 * REST-Controller für Authentifizierungs-Operationen.
 * Verarbeitet Registrierung und Login von Benutzern.
 */
@CrossOrigin(
    origins = {
        "https://frontend-b5ow.onrender.com",
        "http://localhost:5173",
        "http://localhost:3000"
    },
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registriert einen neuen Benutzer.
     *
     * @param request Registrierungsdaten mit Name, Email, Passwort und Hydrationspräferenzen
     * @return Erstelltes Benutzerprofil mit ID
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/auth/register - Registering new user with email: {}", request.getEmail());

        // Prüfe ob E-Mail bereits existiert
        if (userProfileRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthResponse.error("Email already exists"));
        }

        // Erstelle neues Benutzerprofil
        UserProfile profile = new UserProfile();
        profile.setName(request.getName());
        profile.setEmail(request.getEmail());

        // Hash password with BCrypt before storing
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        profile.setPassword(hashedPassword);
        log.debug("Password hashed successfully for user: {}", request.getEmail());

        profile.setWeightKg(request.getWeightKg());
        profile.setActivityLevel(request.getActivityLevel());
        profile.setClimate(request.getClimate());
        profile.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Europe/Berlin");

        UserProfile saved = userProfileRepository.save(profile);

        // Generate simple token (in production use JWT!)
        String token = generateToken(saved.getId());

        // Erstelle AuthResponse mit token und user
        AuthResponse response = new AuthResponse(
            token,
            saved.getId(),
            saved.getEmail(),
            saved.getName()
        );

        log.info("User registered with ID {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Meldet einen Benutzer an.
     *
     * @param request Login-Daten mit Email und Passwort
     * @return Benutzerprofil bei erfolgreicher Authentifizierung
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - User login attempt for email: {}", request.getEmail());

        // Suche Benutzer per E-Mail
        UserProfile profile = userProfileRepository.findByEmail(request.getEmail())
                .orElse(null);

        // Prüfe ob Benutzer existiert
        if (profile == null) {
            log.warn("Login failed: User not found with email {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.error("Invalid email or password"));
        }

        // Validate password using BCrypt
        if (!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            log.warn("Login failed: Invalid password for user {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.error("Invalid email or password"));
        }

        // Generate simple token (in production use JWT!)
        String token = generateToken(profile.getId());

        // Erstelle AuthResponse mit token und user
        AuthResponse response = new AuthResponse(
            token,
            profile.getId(),
            profile.getEmail(),
            profile.getName()
        );

        log.info("User {} logged in successfully", profile.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Holt das Profil eines Benutzers.
     *
     * @param userId Benutzer-ID
     * @return Benutzerprofil
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        log.info("GET /api/auth/profile/{} - Getting user profile", userId);

        UserProfile profile = userProfileRepository.findById(userId)
                .orElse(null);

        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AuthResponse.error("User not found"));
        }

        // Return user info without token for profile endpoint
        AuthResponse response = new AuthResponse();
        response.setUser(new AuthResponse.UserInfo(
            profile.getId(),
            profile.getEmail(),
            profile.getName()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Generiert ein einfaches Token für Authentifizierung.
     * In Production sollte JWT verwendet werden!
     */
    private String generateToken(Long userId) {
        String tokenData = userId + ":" + UUID.randomUUID().toString() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(tokenData.getBytes());
    }
}
