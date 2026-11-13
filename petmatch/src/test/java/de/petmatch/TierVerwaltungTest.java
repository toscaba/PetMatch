
package de.petmatch;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TierVerwaltungTest {

    // TEST 1: Nur verfügbare Tiere werden zurückgegeben.
    @Test
    void testVerfuegbareTiereFiltern() {

        // Drei Tierprofile erstellen.
        Tier hund = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        Tier katze = new Tier(
            2L, 10L, "Mimi", "Katze",
            "Europäisch Kurzhaar", 2, "Ruhig", "mimi.jpg"
        );

        Tier kaninchen = new Tier(
            3L, 10L, "Hoppel", "Kaninchen",
            "Zwergkaninchen", 1, "Verspielt", "hoppel.jpg"
        );

        // Die Katze wird reserviert.
        katze.reservieren();

        // Tierverwaltung mit drei Tieren erstellen.
        TierVerwaltung verwaltung = new TierVerwaltung(
            List.of(hund, katze, kaninchen)
        );

        // Nur verfügbare Tiere abrufen.
        List<Tier> verfuegbareTiere =
            verwaltung.getVerfuegbareTiere();

        // Es müssen genau zwei Tiere verfügbar sein.
        assertEquals(2, verfuegbareTiere.size());

        // Hund und Kaninchen müssen enthalten sein.
        assertTrue(verfuegbareTiere.contains(hund));
        assertTrue(verfuegbareTiere.contains(kaninchen));

        // Die reservierte Katze darf nicht enthalten sein.
        assertFalse(verfuegbareTiere.contains(katze));
    }


    // TEST 2: Eine leere Tierverwaltung gibt
    // eine leere Liste zurück.
    @Test
    void testLeereTierverwaltung() {

        TierVerwaltung verwaltung =
            new TierVerwaltung(List.of());

        List<Tier> verfuegbareTiere =
            verwaltung.getVerfuegbareTiere();

        assertTrue(verfuegbareTiere.isEmpty());
    }

    // TEST 3: Wenn alle Tiere reserviert sind,
    // dürfen keine verfügbaren Tiere angezeigt werden.
    @Test
    void testAlleTiereReserviert() {

        Tier hund = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        Tier katze = new Tier(
            2L, 10L, "Mimi", "Katze",
            "Europäisch Kurzhaar", 2, "Ruhig", "mimi.jpg"
        );

        // Beide Tiere reservieren.
        hund.reservieren();
        katze.reservieren();

        TierVerwaltung verwaltung =
            new TierVerwaltung(List.of(hund, katze));

        // Es darf kein verfügbares Tier geben.
        assertTrue(
            verwaltung.getVerfuegbareTiere().isEmpty()
        );
    }

}
