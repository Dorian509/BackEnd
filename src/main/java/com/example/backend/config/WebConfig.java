package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web-MVC-Konfiguration.
 * Zusätzliche webbezogene Konfigurationen können hier hinzugefügt werden.
 *
 * @author Dorian509
 * @version 1.0
 * @since 2025-01-13
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "https://frontend-b5ow.onrender.com",
                        "https://hydratemate-backend.onrender.com",
                        "http://localhost:5173",
                        "http://localhost:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
