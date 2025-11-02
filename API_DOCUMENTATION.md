# HydrateMate API Documentation

Complete REST API documentation for HydrateMate Backend

**Base URL**: `http://localhost:8080`

---

## Table of Contents

1. [Authentication](#authentication)
2. [Health Check](#health-check)
3. [User Profile](#user-profile)
4. [Hydration Status](#hydration-status)
5. [Water Intake](#water-intake)
6. [Error Responses](#error-responses)
7. [Data Models](#data-models)

---

## Authentication

Currently, the API does not require authentication. Future versions may implement JWT-based authentication.

---

## Health Check

### GET /

Check if the API is running.

**Response**: `200 OK`

```json
{
  "message": "HydrateMate API is running",
  "status": "ok",
  "timestamp": "2024-11-01T12:00:00Z"
}
```

### GET /health

Detailed health check with application metrics.

**Response**: `200 OK`

```json
{
  "status": "UP",
  "timestamp": "2024-11-01T12:00:00Z",
  "service": "HydrateMate Backend",
  "version": "1.0.0"
}
```

---

## User Profile

### POST /api/profile

Create a new user profile with hydration settings.

**Request Body**:

```json
{
  "weightKg": 75,
  "activityLevel": "MEDIUM",
  "climate": "NORMAL",
  "timezone": "Europe/Berlin"
}
```

**Fields**:
- `weightKg` (required): Body weight in kilograms (20-200)
- `activityLevel` (required): Activity level - `LOW`, `MEDIUM`, or `HIGH`
- `climate` (required): Climate condition - `NORMAL` or `HOT`
- `timezone` (optional): User's timezone, defaults to "Europe/Berlin"

**Response**: `201 Created`

```json
{
  "id": 1,
  "weightKg": 75,
  "activityLevel": "MEDIUM",
  "climate": "NORMAL",
  "timezone": "Europe/Berlin"
}
```

**Example**:

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

### GET /api/profile/{id}

Retrieve a user profile by ID.

**Path Parameters**:
- `id`: User profile ID

**Response**: `200 OK`

```json
{
  "id": 1,
  "weightKg": 75,
  "activityLevel": "MEDIUM",
  "climate": "NORMAL",
  "timezone": "Europe/Berlin"
}
```

**Example**:

```bash
curl http://localhost:8080/api/profile/1
```

**Errors**:
- `404 Not Found`: Profile with given ID does not exist

### PUT /api/profile/{id}

Update an existing user profile.

**Path Parameters**:
- `id`: User profile ID

**Request Body**:

```json
{
  "weightKg": 80,
  "activityLevel": "HIGH",
  "climate": "HOT",
  "timezone": "Europe/Berlin"
}
```

**Response**: `200 OK`

```json
{
  "id": 1,
  "weightKg": 80,
  "activityLevel": "HIGH",
  "climate": "HOT",
  "timezone": "Europe/Berlin"
}
```

**Example**:

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

**Errors**:
- `404 Not Found`: Profile with given ID does not exist
- `400 Bad Request`: Invalid input data

---

## Hydration Status

### GET /api/hydration/today/{userId}

Get today's hydration status for a user, including goal, consumed amount, and progress.

**Path Parameters**:
- `userId`: User profile ID

**Response**: `200 OK`

```json
{
  "goalMl": 2700,
  "consumedMl": 1500,
  "remainingMl": 1200,
  "percentageAchieved": 56
}
```

**Fields**:
- `goalMl`: Daily hydration goal in milliliters
- `consumedMl`: Amount consumed today in milliliters
- `remainingMl`: Amount remaining to reach goal
- `percentageAchieved`: Percentage of goal achieved (0-100+)

**Example**:

```bash
curl http://localhost:8080/api/hydration/today/1
```

**Errors**:
- `404 Not Found`: User profile with given ID does not exist

---

## Water Intake

### POST /api/intakes

Record a new water intake event.

**Request Body**:

```json
{
  "userId": 1,
  "volumeMl": 250,
  "source": "GLASS"
}
```

**Fields**:
- `userId` (required): User profile ID
- `volumeMl` (required): Amount of water in milliliters (minimum 1)
- `source` (required): Source of intake - `SIP`, `DOUBLE_SIP`, or `GLASS`

**Response**: `201 Created`

```json
{
  "id": 1,
  "userId": 1,
  "volumeMl": 250,
  "source": "GLASS",
  "timestamp": "2024-11-01T12:30:00Z"
}
```

**Example**:

```bash
curl -X POST http://localhost:8080/api/intakes \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "volumeMl": 250,
    "source": "GLASS"
  }'
```

**Errors**:
- `404 Not Found`: User profile with given ID does not exist
- `400 Bad Request`: Invalid input data

### GET /api/intakes/{userId}/recent

Get recent water intake events for a user.

**Path Parameters**:
- `userId`: User profile ID

**Query Parameters**:
- `limit` (optional): Maximum number of results (default: 10)

**Response**: `200 OK`

```json
[
  {
    "id": 3,
    "userId": 1,
    "volumeMl": 250,
    "source": "GLASS",
    "timestamp": "2024-11-01T14:30:00Z"
  },
  {
    "id": 2,
    "userId": 1,
    "volumeMl": 200,
    "source": "DOUBLE_SIP",
    "timestamp": "2024-11-01T12:15:00Z"
  },
  {
    "id": 1,
    "userId": 1,
    "volumeMl": 100,
    "source": "SIP",
    "timestamp": "2024-11-01T10:00:00Z"
  }
]
```

**Example**:

```bash
# Get last 5 intakes
curl http://localhost:8080/api/intakes/1/recent?limit=5
```

**Errors**:
- `404 Not Found`: User profile with given ID does not exist

### DELETE /api/intakes/{intakeId}

Delete a water intake event.

**Path Parameters**:
- `intakeId`: Intake event ID

**Response**: `204 No Content`

**Example**:

```bash
curl -X DELETE http://localhost:8080/api/intakes/1
```

**Errors**:
- `404 Not Found`: Intake event with given ID does not exist

---

## Error Responses

All error responses follow a standard format:

```json
{
  "timestamp": "2024-11-01T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "UserProfile with ID 999 not found",
  "path": "/api/profile/999"
}
```

### Validation Errors

Validation errors return additional detail about which fields failed validation:

**Response**: `400 Bad Request`

```json
{
  "timestamp": "2024-11-01T12:00:00Z",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "weightKg": "Weight must be at least 20 kg",
    "activityLevel": "Activity level is required"
  },
  "path": "/api/profile"
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| `200 OK` | Successful GET, PUT request |
| `201 Created` | Successful POST request, resource created |
| `204 No Content` | Successful DELETE request |
| `400 Bad Request` | Invalid input, validation errors |
| `404 Not Found` | Resource not found |
| `500 Internal Server Error` | Unexpected server error |

---

## Data Models

### ActivityLevel (Enum)

User's physical activity level, affects daily hydration goal.

| Value | Description | Bonus |
|-------|-------------|-------|
| `LOW` | Minimal physical activity | 0ml |
| `MEDIUM` | Moderate physical activity | +250ml |
| `HIGH` | Intense physical activity | +500ml |

### Climate (Enum)

User's climate conditions, affects hydration needs.

| Value | Description | Bonus |
|-------|-------------|-------|
| `NORMAL` | Normal climate conditions | 0ml |
| `HOT` | Hot climate conditions | +500ml |

### IntakeSource (Enum)

Type/source of water intake.

| Value | Description | Typical Amount |
|-------|-------------|----------------|
| `SIP` | Small sip | ~100ml |
| `DOUBLE_SIP` | Double sip | ~200ml |
| `GLASS` | Full glass | ~250ml |

### Hydration Goal Calculation

The daily hydration goal is calculated using the following formula:

```
Base Goal = Weight (kg) × 35ml

Activity Bonus:
  LOW    → 0ml
  MEDIUM → +250ml
  HIGH   → +500ml

Climate Bonus:
  NORMAL → 0ml
  HOT    → +500ml

Total Goal = (Base Goal + Activity Bonus + Climate Bonus)
             rounded to nearest 50ml
```

**Example Calculation**:

User Profile:
- Weight: 75kg
- Activity: MEDIUM
- Climate: HOT

Calculation:
- Base: 75 × 35 = 2,625ml
- Activity Bonus: +250ml
- Climate Bonus: +500ml
- Total: 2,625 + 250 + 500 = 3,375ml
- **Rounded Goal: 3,350ml**

---

## Testing with cURL

### Complete Workflow Example

```bash
# 1. Check API is running
curl http://localhost:8080/

# 2. Create a user profile
curl -X POST http://localhost:8080/api/profile \
  -H "Content-Type: application/json" \
  -d '{
    "weightKg": 75,
    "activityLevel": "MEDIUM",
    "climate": "NORMAL",
    "timezone": "Europe/Berlin"
  }'
# Save the returned ID (e.g., 1)

# 3. Get today's status (should be 0ml consumed)
curl http://localhost:8080/api/hydration/today/1

# 4. Record a water intake
curl -X POST http://localhost:8080/api/intakes \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "volumeMl": 250,
    "source": "GLASS"
  }'

# 5. Check updated status
curl http://localhost:8080/api/hydration/today/1

# 6. Get recent intakes
curl http://localhost:8080/api/intakes/1/recent?limit=10

# 7. Update profile (e.g., change activity level)
curl -X PUT http://localhost:8080/api/profile/1 \
  -H "Content-Type: application/json" \
  -d '{
    "weightKg": 75,
    "activityLevel": "HIGH",
    "climate": "HOT",
    "timezone": "Europe/Berlin"
  }'

# 8. Check new goal after profile update
curl http://localhost:8080/api/hydration/today/1
```

---

## Postman Collection

You can import these endpoints into Postman for easier testing:

1. Create a new Collection named "HydrateMate API"
2. Set collection variable `baseUrl` = `http://localhost:8080`
3. Add the endpoints listed above
4. Use `{{baseUrl}}` in your request URLs

---

## Rate Limiting

Currently, there is no rate limiting implemented. This may be added in future versions.

---

## Changelog

### Version 1.0.0 (2024-11-01)
- Initial API release
- User profile management
- Water intake tracking
- Daily hydration status
- Personalized goal calculation

---

**Last Updated**: 2024-11-01
**API Version**: 1.0.0
**Documentation Version**: 1.0.0
