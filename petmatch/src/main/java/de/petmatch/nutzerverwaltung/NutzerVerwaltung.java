package de.petmatch.nutzerverwaltung;

public class NutzerVerwaltung {

    private final NutzerRepository repository;
    private final Sitzungsverwaltung sitzungsverwaltung;

    public NutzerVerwaltung(NutzerRepository repository) {
        this.repository = repository;
        this.sitzungsverwaltung = new Sitzungsverwaltung();
    }

    public Nutzer registrieren(
            String name,
            String email,
            String passwort,
            Rolle rolle) {

        Nutzer nutzer = new Nutzer(
                repository.naechsteId(),
                name,
                email,
                passwort,
                rolle
        );

        repository.speichern(nutzer);

        return nutzer;
    }

    public String anmelden(String email, String passwort) {

        if (email == null || email.isBlank()
                || passwort == null || passwort.isEmpty()) {

            throw new IllegalArgumentException(
                    "E-Mail-Adresse oder Passwort ungueltig."
            );
        }

        Nutzer nutzer = repository.findenNachEmail(email);

        if (nutzer == null || !nutzer.passwortPruefen(passwort)) {

            throw new IllegalArgumentException(
                    "E-Mail-Adresse oder Passwort ungueltig."
            );
        }

        return sitzungsverwaltung.sitzungErstellen(
                nutzer.getId()
        );
    }

    public void abmelden(String token) {
        sitzungsverwaltung.sitzungBeenden(token);
    }

    public Nutzer angemeldetenNutzerErmitteln(String token) {

        Long nutzerId = sitzungsverwaltung.nutzerErmitteln(token);

        if (nutzerId == null) {
            return null;
        }

        return repository.finden(nutzerId);
    }

    private Nutzer angemeldetenNutzerPruefen(String token) {

        Nutzer nutzer = angemeldetenNutzerErmitteln(token);

        if (nutzer == null) {
            throw new SecurityException(
                    "Anmeldung erforderlich."
            );
        }

        return nutzer;
    }

    public Nutzer datenAendern(
            String token,
            String name,
            String email) {

        Nutzer nutzer = angemeldetenNutzerPruefen(token);

        repository.datenAendern(
                nutzer.getId(),
                name,
                email
        );

        return nutzer;
    }

    public Nutzer interessentenprofilBearbeiten(
            String token,
            String ueberMich,
            String wohnort,
            String wohnsituation) {

        Nutzer nutzer = angemeldetenNutzerPruefen(token);

        nutzer.interessentenprofilBearbeiten(
                ueberMich,
                wohnort,
                wohnsituation
        );

        return nutzer;
    }

    public Nutzer vermittlerprofilBearbeiten(
            String token,
            boolean istTierheim,
            String tierheimName) {

        Nutzer nutzer = angemeldetenNutzerPruefen(token);

        nutzer.vermittlerprofilBearbeiten(
                istTierheim,
                tierheimName
        );

        return nutzer;
    }
}