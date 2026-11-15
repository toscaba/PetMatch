
package de.petmatch.tierverwaltung;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Speichert Tiere im Arbeitsspeicher (keine Datenbank).
public class TierRepository {

    private final Map<Long, Tier> tiere = new LinkedHashMap<>();
    private long naechsteId = 1;

    public long naechsteId() {
        return naechsteId++;
    }

    public void speichern(Tier tier) {
        tiere.put(tier.getId(), tier);
    }

    // Gibt null zurück, wenn es kein Tier mit dieser ID gibt.
    public Tier finden(long id) {
        return tiere.get(id);
    }

    public List<Tier> alle() {
        return new ArrayList<>(tiere.values());
    }
}
