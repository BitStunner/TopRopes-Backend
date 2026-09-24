# TopRopes Backend

Java 17, Spring Boot, Gradle und PostgreSQL. API-Basis: `/api/v1`.
OpenAPI-Vertrag: `docs/openapi.yaml`.

## Frontend und Backend gemeinsam entwickeln

Standardstruktur:

```text
Git/
├── ring-rumble-react/
├── TopRopes-Backend/
└── topropes-dev/
```

Voraussetzungen: JDK 17, Node.js 22 LTS (mindestens 22.12), npm, Python 3.9+,
Docker und Compose v2.20+ mit laufendem Docker-Daemon. Gradle kommt über den Wrapper.

```bash
cd ../topropes-dev
./dev
```

Das Skript startet PostgreSQL in Docker, wartet auf dessen Healthcheck und startet
Backend (`./gradlew --no-daemon bootRun`) sowie Frontend auf dem Host. Flyway führt
die Migrationen aus; danach wartet das Skript auf `/api/v1/health`.

- Anwendung: <http://localhost:5173>
- Backend-Health: <http://localhost:8080/api/v1/health>
- Frontend: Hot Reload; Java-Änderungen: `./dev` beenden und erneut starten.
- `Strg+C` beendet beide lokalen Server, lässt PostgreSQL und Daten bestehen.
- Datenbank stoppen: `docker compose stop postgres` im gemeinsamen Ordner.

Andere Pfade, Ports, DB-Zugangsdaten und `JWT_SECRET` werden im gemeinsamen Ordner
über `.env` konfiguriert; Vorlage: `.env.example`. Details: `../topropes-dev/README.md`.

## Backend eigenständig starten

Die bisherige Compose-Datei bleibt für den eigenständigen DB-Start verfügbar:

```bash
docker compose up -d --wait postgres
./gradlew bootRun
```

Standard-DB: `jdbc:postgresql://localhost:5432/topropes`, Benutzer und Passwort jeweils
`topropes`. Konfigurierbar über `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_PORT`
und `JWT_SECRET`. Die lokale Standardkonfiguration ist für Entwicklung vorgesehen.
`CORS_ALLOWED_ORIGINS` akzeptiert eine kommagetrennte Liste von Browser-Origins;
standardmäßig sind `http://localhost:5173` und `http://127.0.0.1:5173` erlaubt.
Der gemeinsame Stack passt diese Liste automatisch an `FRONTEND_PORT` an.
Die alte DB-Compose-Datei und den gemeinsamen Stack nicht gleichzeitig verwalten.

## Eigenes Docker-Image

Im Backend-Repository:

```bash
docker build -t topropes-backend:local .
```

Der mehrstufige Build führt `test bootJar` mit dem Gradle-Wrapper aus. Das Laufzeitimage
enthält Java 17, das JAR und curl für den Healthcheck; es läuft ohne Root-Rechte.
Die Datenbank ist ein eigener Service, nicht Bestandteil des Backend-Images.

Für einen eigenständigen Container sind `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` und
`JWT_SECRET` zu setzen. `localhost` im Container bezeichnet den Container selbst;
im gemeinsamen Compose-Netz lautet die DB-URL `jdbc:postgresql://postgres:5432/topropes`.
Das Backend lauscht dort auf Port 8080.

## Gesamten Stack in Docker starten

```bash
cd ../topropes-dev
docker compose build backend            # nur dieses Image
docker compose build                    # beide Images
docker compose up --build -d --wait      # alle drei Services
docker compose logs -f backend
docker compose down                     # Daten bleiben erhalten
```

Das Backend startet erst nach erfolgreichem PostgreSQL-Healthcheck. Das Frontend
leitet `/api` intern an `http://backend:8080` weiter und ist unter
<http://localhost:5173> erreichbar. Alle veröffentlichten Ports sind an Host-Loopback
gebunden. Der Backend-Healthcheck bestätigt den Anwendungsstart nach den Migrationen;
er prüft nicht fortlaufend die DB-Verbindung.

Vor dem Wechsel zu Docker `./dev` mit `Strg+C` beenden. Vor dem Wechsel zurück
`docker compose stop frontend backend` im gemeinsamen Ordner ausführen.

## Vorhandene Datenbank übernehmen

Der gemeinsame Stack verwendet denselben Standard-Projektnamen `topropes-backend`
und dasselbe Volume `topropes-backend_postgres-data` wie die bisherige Konfiguration.
Vor dem ersten Wechsel hier `docker compose stop postgres` ausführen.

Bei einem abweichenden bisherigen Compose-Projektnamen den tatsächlichen Volume-Namen
über `docker inspect topropes-postgres` prüfen und als `POSTGRES_VOLUME` in
`../topropes-dev/.env` setzen. Die gemeinsame README enthält den genauen Befehl.
Vorhandene Daten werden nicht automatisch in ein anderes Volume kopiert.
Geänderte DB-Umgebungsvariablen ändern keine bereits gespeicherten DB-Passwörter.

**`docker compose down -v` löscht das verwendete Datenvolume.** Für einen normalen
Stopp `docker compose down` ohne `-v` verwenden.
