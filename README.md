# HydrateMate Backend API

![Backend CI](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/actions/workflows/backend-ci.yml/badge.svg)
![Code Quality](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/actions/workflows/code-quality.yml/badge.svg)

**Team**: Dorian Gutsche

## Projektbeschreibung

HydrateMate ist ein intelligenter Wasser-Tracker, der den täglichen Wasserbedarf berechnet und beim Erreichen des Ziels unterstützt.

**Features**:
- Personalisierte Bedarfsberechnung basierend auf Gewicht, Aktivitätslevel und Klimazone
- Einfaches Tracking von Wasseraufnahme
- Tägliche Statistiken und Fortschritt
- RESTful API für Frontend-Integration

## Tech Stack

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 25
- **Database**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Build Tool**: Gradle
- **Additional Libraries**:
  - Lombok (Boilerplate reduction)
  - Spring Boot Validation
  - Spring Boot Actuator (Monitoring)
  - HikariCP (Connection pooling)

## Projektstruktur

```
src/main/java/com/example/demo/
├── config/              # Konfigurationsklassen
│   ├── CorsConfig.java
│   └── WebConfig.java
├── controller/          # REST Controller
│   ├── HealthController.java
│   └── HydrationController.java
├── service/             # Business Logic
│   └── HydrationService.java
├── repository/          # Data Access Layer
│   ├── UserProfileRepository.java
│   └── IntakeEventRepository.java
├── model/
│   ├── entity/         # JPA Entities
│   │   ├── UserProfile.java
│   │   └── IntakeEvent.java
│   └── enums/          # Enumerationen
│       ├── ActivityLevel.java (LOW, MEDIUM, HIGH)
│       ├── Climate.java (NORMAL, HOT)
│       └── IntakeSource.java (SIP, DOUBLE_SIP, GLASS)
├── dto/
│   ├── request/        # Request DTOs
│   │   ├── ProfileRequest.java
│   │   └── IntakeRequest.java
│   └── response/       # Response DTOs
│       ├── ProfileResponse.java
│       ├── IntakeResponse.java
│       ├── TodayStatusResponse.java
│       └── ErrorResponse.java
├── exception/          # Exception Handling
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
└── DemoApplication.java  # Main Application
```

## Voraussetzungen

- Java 25 oder höher
- PostgreSQL 12 oder höher
- Gradle 8.x
- (Optional) Docker für PostgreSQL

## Installation & Start

### 1. Repository klonen

```bash
git clone <repository-url>
cd Backend
```

### 2. Datenbank einrichten

#### Option A: Lokale PostgreSQL Installation

```bash
# Datenbank erstellen
psql -U postgres
CREATE DATABASE hydration;
CREATE USER app WITH PASSWORD 'secret';
GRANT ALL PRIVILEGES ON DATABASE hydration TO app;
```

#### Option B: Docker

```bash
docker run --name hydration-db \
  -e POSTGRES_DB=hydration \
  -e POSTGRES_USER=app \
  -e POSTGRES_PASSWORD=secret \
  -p 5432:5432 \
  -d postgres:15
```

### 3. Environment konfigurieren

```bash
# .env Datei aus Beispiel kopieren
cp .env.example .env

# .env mit deinen Daten bearbeiten
nano .env
```

### 4. Anwendung starten

```bash
# Mit Gradle Wrapper
./gradlew bootRun

# Oder JAR bauen und ausführen
./gradlew build
java -jar build/libs/Backend-0.0.1-SNAPSHOT.jar
```

Die API ist verfügbar unter `http://localhost:8080`

## API Dokumentation

### Health Check

#### GET /
Überprüft ob API läuft
```bash
curl http://localhost:8080/
```

Response:
```json
{
  "message": "HydrateMate API is running",
  "status": "ok",
  "timestamp": "2024-11-01T12:00:00Z"
}
```

### Benutzerprofil

#### POST /api/profile
Neues Profil erstellen

```bash
curl -X POST http://localhost:8080/api/profile \
  -H "Content-Type: application/json" \
  -d '{
    "weightKg": 75,
    "activityLevel": "MEDIUM",
    "climate": "NORMAL",
    "timezone": "Europe/Berlin"
  }'
```

**Parameter**:
- `weightKg` (20-200): Gewicht in Kilogramm
- `activityLevel`: `LOW`, `MEDIUM`, oder `HIGH`
- `climate`: `NORMAL` oder `HOT`
- `timezone`: Zeitzone (z.B. "Europe/Berlin")

Response:
```json
{
  "id": 1,
  "weightKg": 75,
  "activityLevel": "MEDIUM",
  "climate": "NORMAL",
  "timezone": "Europe/Berlin"
}
```

#### GET /api/profile/{id}
Profil abrufen

```bash
curl http://localhost:8080/api/profile/1
```

#### PUT /api/profile/{id}
Profil aktualisieren

```bash
curl -X PUT http://localhost:8080/api/profile/1 \
  -H "Content-Type: application/json" \
  -d '{
    "weightKg": 80,
    "activityLevel": "HIGH",
    "climate": "HOT",
    "timezone": "Europe/Berlin"
  }'
```

### Hydration Status

#### GET /api/hydration/today/{userId}
Heutigen Status abrufen

```bash
curl http://localhost:8080/api/hydration/today/1
```

Response:
```json
{
  "goalMl": 2700,
  "consumedMl": 1500,
  "remainingMl": 1200,
  "percentageAchieved": 56
}
```

**Felder**:
- `goalMl`: Tagesziel in Milliliter
- `consumedMl`: Bereits getrunken in Milliliter
- `remainingMl`: Noch zu trinken in Milliliter
- `percentageAchieved`: Erreichungsgrad in Prozent

### Wasseraufnahme

#### POST /api/intakes
Wasseraufnahme erfassen

```bash
curl -X POST http://localhost:8080/api/intakes \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "volumeMl": 250,
    "source": "GLASS"
  }'
```

**Parameter**:
- `userId`: Benutzer-ID
- `volumeMl`: Menge in Milliliter
- `source`: `SIP` (~100ml), `DOUBLE_SIP` (~200ml), oder `GLASS` (~250ml)

Response:
```json
{
  "id": 1,
  "userId": 1,
  "volumeMl": 250,
  "source": "GLASS",
  "timestamp": "2024-11-01T12:30:00Z"
}
```

#### GET /api/intakes/{userId}/recent?limit=10
Letzte Wasseraufnahmen abrufen

```bash
curl http://localhost:8080/api/intakes/1/recent?limit=5
```

#### DELETE /api/intakes/{intakeId}
Wasseraufnahme löschen

```bash
curl -X DELETE http://localhost:8080/api/intakes/1
```

## Bedarfsberechnung

Der tägliche Wasserbedarf wird wie folgt berechnet:

```
Basis-Bedarf = Gewicht (kg) × 35ml

Aktivitäts-Bonus:
- LOW (Niedrig):    0ml
- MEDIUM (Mittel):  +250ml
- HIGH (Hoch):      +500ml

Klima-Bonus:
- NORMAL (Normal):  0ml
- HOT (Heiß):       +500ml

Gesamt-Bedarf = (Basis + Aktivität + Klima)
                gerundet auf 50ml
```

**Beispiel**:
- Gewicht: 75kg
- Aktivität: MEDIUM
- Klima: HOT
- **Bedarf: 3350ml** (75×35 + 250 + 500 = 3375 → 3350ml gerundet)

## Datenbank Schema

### user_profile
```sql
CREATE TABLE user_profile (
    id BIGSERIAL PRIMARY KEY,
    weight_kg INTEGER NOT NULL CHECK (weight_kg >= 20 AND weight_kg <= 200),
    activity_level VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    climate VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    timezone VARCHAR(50) NOT NULL DEFAULT 'Europe/Berlin'
);
```

### intake_event
```sql
CREATE TABLE intake_event (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_profile(id),
    volume_ml INTEGER NOT NULL CHECK (volume_ml >= 1),
    source VARCHAR(20) NOT NULL,
    timestamp_utc TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_timestamp ON intake_event(user_id, timestamp_utc);
```

## Fehlerbehandlung

Alle Fehler geben eine standardisierte Error Response zurück:

```json
{
  "timestamp": "2024-11-01T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "UserProfile with ID 999 not found",
  "path": "/api/profile/999"
}
```

### HTTP Status Codes
- `200 OK` - Erfolgreiche GET/PUT/DELETE Anfrage
- `201 Created` - Erfolgreiche POST Anfrage
- `204 No Content` - Erfolgreiche DELETE Anfrage
- `400 Bad Request` - Validierungsfehler
- `404 Not Found` - Ressource nicht gefunden
- `500 Internal Server Error` - Serverfehler

## Entwicklung

### Tests ausführen

```bash
./gradlew test
```

### Code-Style

Das Projekt verwendet:
- Lombok zur Reduzierung von Boilerplate-Code
- SLF4J für Logging
- Jakarta Validation für Input-Validierung
- Professionelle Package-Struktur nach Best Practices

### Production Build

```bash
# JAR erstellen
./gradlew build

# JAR befindet sich in build/libs/
java -jar build/libs/Backend-0.0.1-SNAPSHOT.jar
```

## Monitoring

Spring Boot Actuator Endpoints:

- `/actuator/health` - Anwendungsstatus
- `/actuator/info` - Anwendungsinformationen
- `/actuator/metrics` - Anwendungsmetriken

## Umgebungsvariablen

| Variable | Beschreibung | Standard |
|----------|--------------|----------|
| `SPRING_DATASOURCE_URL` | PostgreSQL Verbindungs-URL | `jdbc:postgresql://localhost:5432/hydration` |
| `SPRING_DATASOURCE_USERNAME` | Datenbank Benutzername | `app` |
| `SPRING_DATASOURCE_PASSWORD` | Datenbank Passwort | `secret` |
| `PORT` | Server Port | `8080` |
| `APP_CORS_ALLOWED_ORIGINS` | Erlaubte CORS Origins | `http://localhost:5173,http://localhost:3000` |

## Deployment

### Docker Deployment

```dockerfile
# Dockerfile Beispiel
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build und Run:
```bash
./gradlew build
docker build -t hydratemate-backend .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hydration \
  hydratemate-backend
```

### Production Checklist

- [ ] Starkes Datenbank-Passwort setzen
- [ ] CORS für Production-Domain konfigurieren
- [ ] HTTPS/TLS einrichten
- [ ] Production-Logging konfigurieren
- [ ] Connection Pool für Last konfigurieren
- [ ] Datenbank-Backups einrichten
- [ ] Monitoring mit Actuator einrichten
- [ ] `spring.jpa.show-sql=false` setzen

## Troubleshooting

### Datenbankverbindungsprobleme

```bash
# PostgreSQL Status prüfen
pg_isready -h localhost -p 5432

# Verbindung testen
psql -h localhost -U app -d hydration
```

### Port bereits in Verwendung

```bash
# Prozess auf Port 8080 finden
lsof -i :8080

# Prozess beenden
kill -9 <PID>
```

### Build-Probleme

```bash
# Clean Build
./gradlew clean build --refresh-dependencies
```
