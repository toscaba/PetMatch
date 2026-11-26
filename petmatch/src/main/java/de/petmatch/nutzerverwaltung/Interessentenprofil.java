package de.petmatch.nutzerverwaltung;

public class Interessentenprofil {

    private final String ueberMich;
    private final String wohnort;
    private final String wohnsituation;

    public Interessentenprofil(String ueberMich,
                               String wohnort,
                               String wohnsituation) {

        pruefeEingaben(wohnort, wohnsituation);

        this.ueberMich = ueberMich;
        this.wohnort = wohnort;
        this.wohnsituation = wohnsituation;
    }

    private void pruefeEingaben(String wohnort,
                                String wohnsituation) {

        if (wohnort == null || wohnort.isBlank()) {
            throw new IllegalArgumentException(
                    "Der Wohnort darf nicht leer sein."
            );
        }

        if (wohnsituation == null || wohnsituation.isBlank()) {
            throw new IllegalArgumentException(
                    "Die Wohnsituation darf nicht leer sein."
            );
        }
    }

    public String getUeberMich() {
        return ueberMich;
    }

    public String getWohnort() {
        return wohnort;
    }

    public String getWohnsituation() {
        return wohnsituation;
    }
}