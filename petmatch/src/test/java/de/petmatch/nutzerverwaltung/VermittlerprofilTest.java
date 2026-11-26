package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VermittlerprofilTest {

    @Test
    void tierheimprofilKannErstelltWerden() {
        Vermittlerprofil profil = new Vermittlerprofil(
                true,
                "Tierheim Regensburg"
        );

        assertTrue(profil.isTierheim());
        assertEquals("Tierheim Regensburg", profil.getTierheimName());
    }

    @Test
    void privatesVermittlerprofilKannErstelltWerden() {
        Vermittlerprofil profil = new Vermittlerprofil(
                false,
                null
        );

        assertFalse(profil.isTierheim());
        assertNull(profil.getTierheimName());
    }

    @Test
    void tierheimOhneNamenWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vermittlerprofil(true, null)
        );
    }

    @Test
    void tierheimMitLeeremNamenWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vermittlerprofil(true, "")
        );
    }

    @Test
    void tierheimnameNurAusLeerzeichenWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Vermittlerprofil(true, "   ")
        );
    }

    @Test
    void privaterVermittlerMitLeeremTierheimnamenIstGueltig() {
        Vermittlerprofil profil = new Vermittlerprofil(
                false,
                ""
        );

        assertFalse(profil.isTierheim());
    }
}