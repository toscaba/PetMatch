
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
    void testTierFreigeben() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Tier wird zunächst reserviert.
        tier.reservieren();

        assertEquals(TierStatus.RESERVIERT, tier.getStatus());

        // Interessent entscheidet sich gegen Adoption.
        tier.freigeben();

        // Tier ist wieder verfügbar.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());
    }


    // TEST 12: Ein nicht reserviertes Tier darf
    // nicht freigegeben werden.
    @Test
    void testFreigebenOhneReservierung() {

        Tier tier = new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );

        // Das Tier ist noch nicht reserviert.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());

        // Die Freigabe muss verhindert werden.
        assertThrows(IllegalStateException.class, () -> {
            tier.freigeben();
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

        // Das Tier ist jetzt vermittelt.
        assertEquals(TierStatus.VERMITTELT, tier.getStatus());
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

    // TEST 15: Eine Tierart darf nicht null sein.
    @Test
    void testTierartNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () ->
                        new Tier(
                                1L, 10L, "Bello", null,
                                "Labrador", 3, "Freundlich", "bello.jpg"
                        )
        );

        assertEquals("Die Tierart darf nicht leer sein.", ex.getMessage());
    }


    // TEST 16: Ein vermitteltes Tier kann weder reserviert,
    // freigegeben noch erneut adoptiert werden.
    @Test
    void testVermitteltesTier() {

        Tier tier = neuesTier();
        tier.reservieren();
        tier.adoptieren();

        assertThrows(IllegalStateException.class, tier::reservieren);
        assertThrows(IllegalStateException.class, tier::freigeben);
        assertThrows(IllegalStateException.class, tier::adoptieren);

        // Der Status bleibt unverändert.
        assertEquals(TierStatus.VERMITTELT, tier.getStatus());
    }


    // TEST 17: Ein Tier kann aus jedem Status heraus gelöscht werden.
    @Test
    void testTierLoeschen() {

        Tier verfuegbar = neuesTier();

        Tier reserviert = neuesTier();
        reserviert.reservieren();

        Tier vermittelt = neuesTier();
        vermittelt.reservieren();
        vermittelt.adoptieren();

        verfuegbar.loeschen();
        reserviert.loeschen();
        vermittelt.loeschen();

        assertEquals(TierStatus.GELOESCHT, verfuegbar.getStatus());
        assertEquals(TierStatus.GELOESCHT, reserviert.getStatus());
        assertEquals(TierStatus.GELOESCHT, vermittelt.getStatus());
    }


    // TEST 18: Ein gelöschtes Tier kann weder erneut gelöscht
    // noch reserviert werden.
    @Test
    void testGeloeschtesTier() {

        Tier tier = neuesTier();
        tier.loeschen();

        assertThrows(IllegalStateException.class, tier::loeschen);
        assertThrows(IllegalStateException.class, tier::reservieren);

        assertEquals(TierStatus.GELOESCHT, tier.getStatus());
    }


    // TEST 19: Ein Tierprofil kann geändert werden.
    @Test
    void testTierAendern() {

        Tier tier = neuesTier();

        tier.aendern("Rocky", "Hund", "Boxer", 5, "Verspielt", "rocky.jpg");

        assertEquals("Rocky", tier.getName());
        assertEquals("Hund", tier.getTierart());
        assertEquals("Boxer", tier.getRasse());
        assertEquals(5, tier.getAlter());
        assertEquals("Verspielt", tier.getCharakter());
        assertEquals("rocky.jpg", tier.getFoto());

        // Der Status ändert sich dabei nicht.
        assertEquals(TierStatus.ZU_VERMITTELN, tier.getStatus());
    }


    // TEST 20: Ungültige Änderungen werden abgelehnt,
    // das Tierprofil bleibt unverändert.
    @Test
    void testTierAendernUngueltig() {

        Tier tier = neuesTier();

        assertThrows(IllegalArgumentException.class, () ->
            tier.aendern("", "Hund", "Boxer", 5, "Verspielt", "rocky.jpg")
        );

        assertEquals("Bello", tier.getName());
        assertEquals("Labrador", tier.getRasse());
    }


    // Hilfsmethode: Erstellt ein gültiges Standard-Tier.
    private Tier neuesTier() {
        return new Tier(
            1L, 10L, "Bello", "Hund",
            "Labrador", 3, "Freundlich", "bello.jpg"
        );
    }

}
