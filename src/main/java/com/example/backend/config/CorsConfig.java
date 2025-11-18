package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) Konfiguration.
 * Erlaubt Frontend-Anwendungen, Anfragen an diese API zu stellen.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Erlaubte Origins (Frontend-URLs)
        configuration.setAllowedOrigins(Arrays.asList(
                "https://frontend-b5ow.onrender.com",
                "https://hydratemate-backend.onrender.com",
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        // Erlaubte HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Erlaubte Header
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With"
        ));

        // Erlaube Credentials (Cookies, Authorization-Header)
        configuration.setAllowCredentials(true);

        // Wie lange Preflight-Anfragen gecacht werden können (1 Stunde)
        configuration.setMaxAge(3600L);

        // Exponiere Header für das Frontend
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
