package com.example.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * DataSource-Konfiguration für Render.com Deployment.
 * Konvertiert die Render-URL (postgresql://) automatisch zu JDBC-Format (jdbc:postgresql://).
 */
@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/hydration}")
    private String databaseUrl;

    @Value("${SPRING_DATASOURCE_USERNAME:app}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD:secret}")
    private String password;

    @Bean
    public DataSource dataSource() {
        // Konvertiere Render.com URL Format zu JDBC URL Format
        String jdbcUrl = convertToJdbcUrl(databaseUrl);

        log.info("Configuring DataSource with URL: {}", jdbcUrl.replaceAll(":[^:@]+@", ":***@"));

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        // Connection Pool Settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setValidationTimeout(5000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }

    /**
     * Konvertiert Render.com PostgreSQL URL zu JDBC URL Format.
     *
     * @param url Die Original-URL (postgresql:// oder jdbc:postgresql://)
     * @return JDBC-kompatible URL (jdbc:postgresql://)
     */
    private String convertToJdbcUrl(String url) {
        if (url == null || url.isEmpty()) {
            log.warn("Database URL is null or empty, using default");
            return "jdbc:postgresql://localhost:5432/hydration";
        }

        // Wenn URL bereits mit jdbc: beginnt, direkt zurückgeben
        if (url.startsWith("jdbc:")) {
            return url;
        }

        // Wenn URL mit postgresql:// beginnt, jdbc: Präfix hinzufügen
        if (url.startsWith("postgresql://")) {
            String jdbcUrl = "jdbc:" + url;
            log.info("Converted Render URL to JDBC format");
            return jdbcUrl;
        }

        // Fallback: URL unverändert zurückgeben
        log.warn("URL format not recognized, using as-is: {}", url);
        return url;
    }
}
