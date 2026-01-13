package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Haupt-Anwendungsklasse für das HydrateMate Backend.
 * Konfiguriert Spring Boot und stellt die Password-Encoder-Bean bereit.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-13
 */
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	/**
	 * Password Encoder Bean für BCrypt Hashing.
	 * Verwendet für sichere Passwort-Speicherung mit automatischem Salting.
	 *
	 * @return BCryptPasswordEncoder mit Standard-Strength (10)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
