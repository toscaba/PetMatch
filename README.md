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
Repo und ersten Branch anlegen: Tosca
1. Fassung Readme: Tosca
Branch anlegen, Änderungen und PR: Melanie