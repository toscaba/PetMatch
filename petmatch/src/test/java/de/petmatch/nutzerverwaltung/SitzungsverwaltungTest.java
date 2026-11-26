package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SitzungsverwaltungTest {

    @Test
    void sitzungKannErstelltWerden() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        String token = verwaltung.sitzungErstellen(1L);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void sitzungIstNutzerZugeordnet() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        String token = verwaltung.sitzungErstellen(1L);

        assertEquals(1L, verwaltung.nutzerErmitteln(token));
    }

    @Test
    void sitzungstokensSindEindeutig() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        String token1 = verwaltung.sitzungErstellen(1L);
        String token2 = verwaltung.sitzungErstellen(1L);

        assertNotEquals(token1, token2);
    }

    @Test
    void unbekanntesTokenLiefertNull() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        assertNull(
                verwaltung.nutzerErmitteln("ungueltiges-token")
        );
    }

    @Test
    void sitzungKannBeendetWerden() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        String token = verwaltung.sitzungErstellen(1L);

        verwaltung.sitzungBeenden(token);

        assertNull(verwaltung.nutzerErmitteln(token));
    }

    @Test
    void mehrereNutzerKoennenSitzungenBesitzen() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        String token1 = verwaltung.sitzungErstellen(1L);
        String token2 = verwaltung.sitzungErstellen(2L);

        assertEquals(1L, verwaltung.nutzerErmitteln(token1));
        assertEquals(2L, verwaltung.nutzerErmitteln(token2));
    }

    @Test
    void abmeldungBeeinflusstAndereSitzungenNicht() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        String token1 = verwaltung.sitzungErstellen(1L);
        String token2 = verwaltung.sitzungErstellen(1L);

        verwaltung.sitzungBeenden(token1);

        assertNull(verwaltung.nutzerErmitteln(token1));
        assertEquals(1L, verwaltung.nutzerErmitteln(token2));
    }

    @Test
    void fehlendeNutzerIdWirdAbgelehnt() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.sitzungErstellen(null)
        );
    }

    @Test
    void fehlendesTokenLiefertNull() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        assertNull(verwaltung.nutzerErmitteln(null));
    }

    @Test
    void unbekannteSitzungKannFehlerfreiBeendetWerden() {
        Sitzungsverwaltung verwaltung = new Sitzungsverwaltung();

        assertDoesNotThrow(
                () -> verwaltung.sitzungBeenden("unbekannt")
        );
    }
}