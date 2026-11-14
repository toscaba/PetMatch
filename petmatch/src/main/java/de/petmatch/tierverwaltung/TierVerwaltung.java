
package de.petmatch.tierverwaltung;

import java.util.List;

public class TierVerwaltung {

    // Liste aller verwalteten Tiere
    private final List<Tier> tiere;

    // Konstruktor: Übernimmt die Tierprofile
    public TierVerwaltung(List<Tier> tiere) {
        this.tiere = List.copyOf(tiere);
    }

    // Gibt ausschließlich verfügbare Tiere zurück.
    public List<Tier> getVerfuegbareTiere() {

        return tiere.stream()
            .filter(tier ->
                tier.getStatus() == TierStatus.ZU_VERMITTELN
            )
            .toList();
    }
}
