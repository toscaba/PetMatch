package de.petmatch.nutzerverwaltung;

public class Nutzer {

    private final Long id;
    private String name;
    private String email;
    private Rolle rolle;
    private final String passwortHash;

    private Interessentenprofil interessentenprofil;
    private Vermittlerprofil vermittlerprofil;

    public Nutzer(
            Long id,
            String name,
            String email,
            String passwort,
            Rolle rolle) {

        pruefeEingaben(name, email, passwort, rolle);

        this.id = id;
        this.name = name;
        this.email = email;
        this.rolle = rolle;
        this.passwortHash = PasswortHasher.hashen(passwort);
    }

    private void pruefeEingaben(
            String name,
            String email,
            String passwort,
            Rolle rolle) {

        pruefeNutzerdaten(name, email);

        if (passwort == null || passwort.length() < 8) {
            throw new IllegalArgumentException(
                    "Das Passwort muss mindestens 8 Zeichen enthalten."
            );
        }

        if (rolle == null) {
            throw new IllegalArgumentException(
                    "Eine Rolle muss ausgewaehlt werden."
            );
        }
    }

    private void pruefeNutzerdaten(String name, String email) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Der Name darf nicht leer sein."
            );
        }

        if (email == null
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            throw new IllegalArgumentException(
                    "Die E-Mail-Adresse ist ungueltig."
            );
        }
    }

    void datenAendern(String name, String email) {

        pruefeNutzerdaten(name, email);

        this.name = name;
        this.email = email;
    }

    public boolean passwortPruefen(String passwort) {
        return PasswortHasher.pruefen(passwort, passwortHash);
    }

    public void interessentenprofilErstellen(
            String ueberMich,
            String wohnort,
            String wohnsituation) {

        if (rolle != Rolle.INTERESSENT) {
            throw new IllegalStateException(
                    "Nur Interessenten duerfen ein Interessentenprofil erstellen."
            );
        }

        if (interessentenprofil != null) {
            throw new IllegalStateException(
                    "Es existiert bereits ein Interessentenprofil."
            );
        }

        this.interessentenprofil = new Interessentenprofil(
                ueberMich,
                wohnort,
                wohnsituation
        );
    }

    public void vermittlerprofilErstellen(
            boolean istTierheim,
            String tierheimName) {

        if (rolle != Rolle.VERMITTLER) {
            throw new IllegalStateException(
                    "Nur Vermittler duerfen ein Vermittlerprofil erstellen."
            );
        }

        if (vermittlerprofil != null) {
            throw new IllegalStateException(
                    "Es existiert bereits ein Vermittlerprofil."
            );
        }

        this.vermittlerprofil = new Vermittlerprofil(
                istTierheim,
                tierheimName
        );
    }

    void interessentenprofilBearbeiten(
            String ueberMich,
            String wohnort,
            String wohnsituation) {

        if (rolle != Rolle.INTERESSENT) {
            throw new IllegalStateException(
                    "Nur Interessenten duerfen dieses Profil bearbeiten."
            );
        }

        this.interessentenprofil = new Interessentenprofil(
                ueberMich,
                wohnort,
                wohnsituation
        );
    }

    void vermittlerprofilBearbeiten(
            boolean istTierheim,
            String tierheimName) {

        if (rolle != Rolle.VERMITTLER) {
            throw new IllegalStateException(
                    "Nur Vermittler duerfen dieses Profil bearbeiten."
            );
        }

        this.vermittlerprofil = new Vermittlerprofil(
                istTierheim,
                tierheimName
        );
    }

    public Interessentenprofil getInteressentenprofil() {
        return interessentenprofil;
    }

    public Vermittlerprofil getVermittlerprofil() {
        return vermittlerprofil;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Rolle getRolle() {
        return rolle;
    }
}