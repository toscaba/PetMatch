# Domänenmodell PetMatch (Übung 3, Aufgabe 2)

## Klassendiagramm

![Klassendiagramm PetMatch](klassendiagramm.jpg)


## Von Events zum Modell

| Event (Miro)                                     | Im Modell                                                                                                                                                                     |
|--------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Nutzer hat sich registriert                      | `Nutzer.registrieren()`                                                                                                                                                       |
| Interessent hat Profil mit Daten gefüllt         | `Interessentenprofil` (ueberMich, wohnort, wohnsituation)                                                                                                                     |
| Tierprofil wurde erstellt                        | neues `Tier` mit Status `ZU_VERMITTELN`                                                                                                                                       |
| Termin wurde angefragt                           | neue `Terminanfrage` mit Status `ANGEFRAGT`                                                                                                                                   |
| Termin wurde bestätigt                           | `Terminanfrage.bestaetigen()`                                                                                                                                                 |
| Termin wurde abgelehnt                           | `Terminanfrage.ablehnen()`                                                                                                                                                    |
| Tier wurde reserviert und aus der Suche entfernt | `Tier.reservieren()` → Status `RESERVIERT`                                                                                                                                    |
| Tier wurde nicht adoptiert                       | `Tier.nichtAdoptiert()` → zurück zu `ZU_VERMITTELN`                                                                                                                           |
| Tier wurde adoptiert / Inserat wurde gelöscht    | `Tier.adoptieren()` → Tier und seine Terminanfragen werden gelöscht                                                                                                           |
| Termin hat stattgefunden                         | kein eigener Status nötig, weil die Kommunikation nicht im System stattfindet. Danach folgt „adoptiert“  --> löschen oder „nicht adoptiert“ --> wieder Status `ZU_VERMITTELN` |
