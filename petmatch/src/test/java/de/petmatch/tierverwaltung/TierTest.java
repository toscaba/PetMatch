
package de.petmatch.tierverwaltung;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TierTest {

    // TEST 1: Ein gültiges Tierprofil wird erstellt.
    @Test
    void testTierErstellen() {
        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        assertEquals(1L, tier.getId());
        assertEquals(10L, tier.getNutzerId());
        assertEquals("Bello", tier.getName());
        assertEquals("Hund", tier.getTierart());
        assertEquals("Labrador", tier.getRasse());
        assertEquals(3, tier.getAlter());
        assertEquals("Freundlich", tier.getCharakter());
        assertEquals("bello.jpg", tier.getFoto());
    }

    // TEST 2: Ein leerer Tiername ist nicht erlaubt.
    @Test
    void testLeererName() {
        assertThrows(IllegalArgumentException.class, () ->
            new Tier(
                1L, 10L, "", "Hund",
                "Labrador", 3, "Freundlich", "bello.jpg"
            )
        );
    }

    // TEST 3: Ein Tiername darf nicht null sein.
    @Test
    void testNameNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Tier(
                1L, 10L, null, "Hund",
                "Labrador", 3, "Freundlich", "bello.jpg"
            )
        );
    }

    // TEST 4: Eine leere Tierart ist nicht erlaubt.
    @Test
    void testLeereTierart() {
        assertThrows(IllegalArgumentException.class, () ->
            new Tier(
                1L, 10L, "Bello", "",
                "Labrador", 3, "Freundlich", "bello.jpg"
            )
        );
    }

    // TEST 5: Ein negatives Alter ist nicht erlaubt.
    @Test
    void testNegativesAlter() {
        assertThrows(IllegalArgumentException.class, () ->
            new Tier(
                1L, 10L, "Bello", "Hund",
                "Labrador", -1, "Freundlich", "bello.jpg"
            )
        );
    }

    // TEST 6: Ein Tier darf 0 Jahre alt sein.
    @Test
    void testAlterNull() {
        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 0, "Freundlich", "bello.jpg"
        );

        assertEquals(0, tier.getAlter());
    }

    // TEST 7: Ein neues Tier ist automatisch zu vermitteln.
    @Test
    void testAnfangsstatus() {
        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());
    }

    // TEST 8: Ein Tier wird dem richtigen Vermittler zugeordnet.
    @Test
    void testVermittlerZuordnung() {
        Tier tier = new Tier(
            1L, 25L, "Mimi", "Katze",
            "Europäisch Kurzhaar", 2, "Ruhig", "mimi.jpg"
        );

        assertEquals(25L, tier.getNutzerId());
    }


    // TEST 9: Ein verfügbares Tier kann reserviert werden.
    @Test
    void testTierReservieren() {
        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        tier.reservieren();

        assertEquals(TierStatus.RESERVIERT, tier.getStatus());
    }


    // TEST 10: Ein bereits reserviertes Tier darf
    // nicht erneut reserviert werden.
    @Test
    void testDoppelteReservierung() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Erste Reservierung ist erlaubt.
        tier.reservieren();

        // Zweite Reservierung muss verhindert werden.
        assertThrows(IllegalStateException.class, () -> {
            tier.reservieren();
        });

        // Der Status bleibt unverändert.
        assertEquals(TierStatus.RESERVIERT, tier.getStatus());
    }


    // TEST 11: Ein nicht adoptiertes Tier wird
    // wieder zur Vermittlung freigegeben.
    @Test
    void testTierNichtAdoptiert() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Tier wird zunächst reserviert.
        tier.reservieren();

        assertEquals(TierStatus.RESERVIERT, tier.getStatus());

        // Interessent entscheidet sich gegen Adoption.
        tier.nichtAdoptiert();

        // Tier ist wieder verfügbar.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());
    }


    // TEST 12: Ein nicht reserviertes Tier darf
    // nicht als "nicht adoptiert" freigegeben werden.
    @Test
    void testNichtAdoptiertOhneReservierung() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Das Tier ist noch nicht reserviert.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());

        // Die Freigabe muss verhindert werden.
        assertThrows(IllegalStateException.class, () -> {
            tier.nichtAdoptiert();
        });

        // Der Status darf sich nicht verändern.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());
    }


    // TEST 13: Ein reserviertes Tier kann adoptiert werden.
    @Test
    void testTierAdoptieren() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Tier wird zunächst reserviert.
        tier.reservieren();

        assertEquals(TierStatus.RESERVIERT, tier.getStatus());

        // Adoption wird durchgeführt.
        assertDoesNotThrow(() -> {
            tier.adoptieren();
        });
    }


    // TEST 14: Ein nicht reserviertes Tier
    // darf nicht adoptiert werden.
    @Test
    void testAdoptionOhneReservierung() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Das Tier ist zunächst verfügbar.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());

        // Eine Adoption ohne Reservierung ist nicht erlaubt.
        assertThrows(IllegalStateException.class, () -> {
            tier.adoptieren();
        });

        // Der Status bleibt unverändert.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());
    }



}
