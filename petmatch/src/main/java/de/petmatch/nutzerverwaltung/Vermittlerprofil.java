package de.petmatch.nutzerverwaltung;

public class Vermittlerprofil {

    private final boolean istTierheim;
    private final String tierheimName;

    public Vermittlerprofil(boolean istTierheim,
                            String tierheimName) {

        pruefeEingaben(istTierheim, tierheimName);

        this.istTierheim = istTierheim;
        this.tierheimName = tierheimName;
    }

    private void pruefeEingaben(boolean istTierheim,
                                String tierheimName) {

        if (istTierheim &&
                (tierheimName == null || tierheimName.isBlank())) {

            throw new IllegalArgumentException(
                    "Der Name des Tierheims darf nicht leer sein."
            );
        }
    }

    public boolean isTierheim() {
        return istTierheim;
    }

    public String getTierheimName() {
        return tierheimName;
    }
}