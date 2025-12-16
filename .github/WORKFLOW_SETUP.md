# GitHub Actions CI/CD Setup

## 📋 Übersicht

Dieses Projekt verwendet GitHub Actions für automatisierte Tests und Code-Quality-Checks.

## 🔧 Workflows

### 1. Backend CI (`backend-ci.yml`)
**Trigger:** Push/PR auf `main` und `develop` Branches

**Schritte:**
- ✅ Checkout Code
- ✅ Setup JDK 21 (Temurin Distribution)
- ✅ Cache Gradle Dependencies
- ✅ Run Tests (`./gradlew test`)
- ✅ Build Application (`./gradlew build`)
- ✅ Upload Test Results (XML)
- ✅ Upload Test Reports (HTML)
- ✅ Upload JAR Artifact

**Test Database:** H2 In-Memory (automatisch konfiguriert)

### 2. Code Quality (`code-quality.yml`)
**Trigger:** Push/PR auf `main` und `develop` Branches

**Schritte:**
- ✅ Code Formatting Check
- ✅ Full Build Verification

## 📦 Artifacts

Nach jedem Workflow-Run werden folgende Artifacts gespeichert:

| Artifact Name | Beschreibung | Retention |
|--------------|--------------|-----------|
| `test-results` | JUnit XML Test Results | 30 Tage |
| `test-reports` | HTML Test Reports | 30 Tage |
| `app-jar` | Gebaute JAR-Datei | 7 Tage |

## 🎯 Status Badges

Die folgenden Badges wurden zur README hinzugefügt:

```markdown
![Backend CI](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/actions/workflows/backend-ci.yml/badge.svg)
![Code Quality](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/actions/workflows/code-quality.yml/badge.svg)
```

**⚠️ WICHTIG:** Ersetze `YOUR_USERNAME` und `YOUR_REPO_NAME` mit deinen tatsächlichen GitHub-Werten!

Beispiel:
```markdown
![Backend CI](https://github.com/doriangutsche/Backend/actions/workflows/backend-ci.yml/badge.svg)
```

## 🚀 Aktivierung

### 1. Lokale Änderungen committen

```bash
# Status überprüfen
git status

# Alle GitHub Actions Dateien hinzufügen
git add .github/
git add README.md

# Commit erstellen
git commit -m "Add GitHub Actions CI/CD workflows

- Add backend-ci.yml for automated testing
- Add code-quality.yml for quality checks
- Add workflow status badges to README
- Configure JUnit 5 tests with H2 database"

# Zum Remote Repository pushen
git push origin main
```

### 2. Alternative: Einzelne Commits

```bash
# Workflow-Dateien hinzufügen
git add .github/workflows/backend-ci.yml
git add .github/workflows/code-quality.yml
git commit -m "Add GitHub Actions workflows for CI/CD"

# README aktualisieren
git add README.md
git commit -m "Add workflow status badges to README"

# Dokumentation hinzufügen
git add .github/WORKFLOW_SETUP.md
git commit -m "Add GitHub Actions documentation"

# Alle Commits pushen
git push origin main
```

## 📊 Workflow-Status überprüfen

### Auf GitHub:

1. **Repository-Übersicht:**
   - Gehe zu: `https://github.com/YOUR_USERNAME/YOUR_REPO_NAME`
   - Siehst du die grünen/roten Badges oben in der README

2. **Actions Tab:**
   - Klicke auf den "Actions" Tab
   - Hier siehst du alle Workflow-Runs
   - Grüner Haken ✅ = Erfolg
   - Rotes X ❌ = Fehler

3. **Einzelnen Workflow öffnen:**
   - Klicke auf einen Workflow-Run
   - Siehst du alle Steps mit Logs
   - Bei Fehlern: Klicke auf den fehlgeschlagenen Step für Details

4. **Artifacts herunterladen:**
   - Öffne einen erfolgreichen Workflow-Run
   - Scrolle nach unten zu "Artifacts"
   - Klicke zum Download (z.B. Test Reports)

### Per API:

```bash
# Status des letzten Workflow-Runs
curl -H "Accept: application/vnd.github+json" \
     https://api.github.com/repos/YOUR_USERNAME/YOUR_REPO_NAME/actions/runs

# Mit GitHub CLI (gh)
gh run list
gh run view --log
```

## 🐛 Troubleshooting

### Tests schlagen fehl in GitHub Actions

**Problem:** Tests funktionieren lokal, aber nicht in GitHub Actions

**Lösung:**
1. Prüfe Test-Logs im Actions Tab
2. Stelle sicher, dass H2-Dependency in `build.gradle` vorhanden ist
3. Prüfe `src/test/resources/application.properties` für Test-Config

### Build schlägt fehl

**Problem:** `./gradlew build` schlägt fehl

**Lösung:**
```bash
# Lokal testen
./gradlew clean build --stacktrace

# Cache leeren falls nötig
./gradlew clean --no-daemon
```

### Permissions-Fehler

**Problem:** `Permission denied: ./gradlew`

**Lösung:** Der Workflow enthält bereits `chmod +x gradlew`

Wenn das Problem weiterhin besteht:
```bash
git update-index --chmod=+x gradlew
git commit -m "Fix gradlew permissions"
git push
```

## 📝 Nächste Schritte

- [ ] Badges in README mit echten Werten aktualisieren
- [ ] Workflows nach erstem Push testen
- [ ] Test Coverage hinzufügen (JaCoCo Plugin)
- [ ] Docker Build & Push hinzufügen
- [ ] Deployment Workflow erstellen

## 📚 Weitere Ressourcen

- [GitHub Actions Dokumentation](https://docs.github.com/en/actions)
- [Gradle Actions Setup](https://github.com/gradle/actions)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
