# PetMatch
Eine Anwendung, die Tierheime bei der Vermittlung von Tieren unterstützt und passende Interessenten findet.

# Git-Handout für Anfänger

Dieses Handout erklärt die wichtigsten Grundlagen von Git
und zeigt, wie man im Team mit GitHub arbeitet.

## 1. Was ist Git?

Git ist ein Versionskontrollsystem. Es speichert Änderungen
an Dateien und ermöglicht es, frühere Versionen nachzuvollziehen
oder bei Bedarf wiederherzustellen.

Mehrere Personen können gemeinsam an einem Projekt arbeiten,
ohne ihre Änderungen ständig manuell austauschen zu müssen.

### Warum sollte man Git verwenden?

- Änderungen und frühere Versionen nachvollziehen
- Gemeinsam im Team an Projekten arbeiten
- Verschiedene Lösungsansätze parallel entwickeln
- Fehler leichter finden und rückgängig machen
- Änderungen vor der Übernahme überprüfen

## 2. Wichtige Begriffe

- **Repository:** Projektordner mit Dateien und Änderungshistorie.
- **Commit:** Gespeicherter Zwischenstand mit einer Nachricht.
- **Branch:** Eigener Arbeitszweig für Änderungen.
- **Push:** Lokale Commits zu einem entfernten Repository hochladen.
- **Pull:** Änderungen aus einem entfernten Repository abrufen.
- **Merge:** Änderungen aus verschiedenen Branches zusammenführen.
- **Pull Request:** Vorschlag, Änderungen zu prüfen und in einen anderen Branch zu übernehmen.


## 3. Die wichtigsten Git-Befehle

| Befehl | Erklärung |
| --- | --- |
| `git init` | Erstellt ein neues lokales Git-Repository. |
| `git clone URL` | Kopiert ein vorhandenes Repository auf den Computer. |
| `git status` | Zeigt den aktuellen Zustand der Dateien. |
| `git add DATEI` | Bereitet eine Datei für den nächsten Commit vor. |
| `git commit -m "Nachricht"` | Speichert einen Zwischenstand mit einer Nachricht. |
| `git push` | Lädt lokale Commits zum Remote-Repository hoch. |
| `git pull` | Holt Änderungen aus dem Remote-Repository und integriert sie. |
| `git branch` | Zeigt die vorhandenen lokalen Branches an. |
| `git switch BRANCH` | Wechselt zu einem anderen Branch. |
| `git merge BRANCH` | Führt Änderungen eines anderen Branches zusammen. |

### Beispiel für einen typischen Ablauf

1. Dateien bearbeiten.
2. Mit `git status` die Änderungen prüfen.
3. Mit `git add` die gewünschten Dateien vorbereiten.
4. Mit `git commit` die Änderungen speichern.
5. Mit `git push` die Commits zu GitHub hochladen.
6. Pull Request (PR) direkt in der Weboberfläche anlegen

### Contribution
1. Repo und ersten Branch anlegen: Tosca
2. Erste Fassung Readme: Tosca
3. Branch anlegen, Änderungen und PR: Melanie

## 4. CI/CD-Pipeline mit GitHub Actions

### Ziel und verwendete Technologien

Für das Projekt PetMatch wurde eine Continuous-Integration-Pipeline (CI) mit GitHub Actions eingerichtet. Ziel ist es, Änderungen am Quellcode automatisch zu überprüfen und mögliche Fehler frühzeitig zu erkennen.

GitHub Actions wurde ausgewählt, da das Projekt bereits auf GitHub verwaltet wird und die Plattform direkt in das bestehende Repository integriert ist. Dadurch ist keine zusätzliche CI-Software erforderlich.

Folgende Technologien werden verwendet:

- **GitHub Actions:** Automatisierung der Build- und Testprozesse
- **Java 17:** Ausführungsumgebung des Projekts
- **Apache Maven:** Verwaltung der Abhängigkeiten und Durchführung des Builds
- **JUnit 5:** Automatisierte Unit-Tests

### Funktionsweise der CI-Pipeline

Die Pipeline ist in der Datei `.github/workflows/maven.yml` definiert und wird bei jedem Push auf einen Branch sowie bei Pull Requests auf `main` automatisch gestartet.

Der Workflow führt folgende Schritte aus:

1. Das Repository wird mithilfe von `actions/checkout@v4` heruntergeladen.
2. Java 17 wird mithilfe von `actions/setup-java@v4` eingerichtet.
3. Maven lädt die benötigten Abhängigkeiten und baut das Projekt.
4. Mit `mvn --batch-mode clean verify` werden die automatisierten Tests ausgeführt.
5. GitHub Actions zeigt an, ob der Workflow erfolgreich abgeschlossen wurde.

Die Ergebnisse können im GitHub-Repository unter dem Reiter **Actions** eingesehen werden.

### Automatisierte Tests

Im Rahmen der testgetriebenen Entwicklung wurden JUnit-Tests für die Tierverwaltung implementiert. Insgesamt wurden 18 Tests lokal erfolgreich ausgeführt.

Die Tests überprüfen unter anderem:

- Erstellung von Tierprofilen und Validierung der Eingabewerte
- Automatische Vergabe des Status `ZU_VERMITTELN`
- Reservierung und erneute Freigabe von Tieren
- Behandlung ungültiger Statuswechsel
- Prüfung der Voraussetzungen für eine Adoption
- Filterung verfügbarer Tiere
- Verhalten bei leeren Tierlisten

Die Tests werden bei jedem Pipeline-Durchlauf automatisch ausgeführt.

### Branching und Pull Requests

Die Entwicklung erfolgt über separate Git-Branches. Änderungen werden zunächst auf einem eigenen Branch implementiert und anschließend über einen Pull Request zur Übernahme in `main` vorgeschlagen.

GitHub Actions überprüft die Änderungen automatisch. Pull Requests sollen erst zusammengeführt werden, wenn die automatisierten Prüfungen erfolgreich abgeschlossen wurden.

### Continuous Deployment

Aktuell wird Continuous Integration umgesetzt. Ein automatisiertes Deployment ist noch nicht eingerichtet.

Für eine spätere Bereitstellung der Anwendung könnten beispielsweise Docker und eine Cloud-Plattform wie Render oder Railway eingesetzt werden. Voraussetzung dafür ist eine entsprechend erweiterte, ausführbare Anwendung.

Nach erfolgreichem Build und bestandenen Tests könnte zukünftig automatisch ein Docker-Image erstellt und auf einer geeigneten Hosting-Plattform bereitgestellt werden.