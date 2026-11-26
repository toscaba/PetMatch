package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NutzerTest {

    @Test
    void gueltigerNutzerKannErstelltWerden() {
        Nutzer nutzer = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertEquals(1L, nutzer.getId());
        assertEquals("Max Mustermann", nutzer.getName());
        assertEquals("max@example.com", nutzer.getEmail());
        assertEquals(Rolle.INTERESSENT, nutzer.getRolle());
    }

    @Test
    void vermittlerKannErstelltWerden() {
        Nutzer nutzer = new Nutzer(
                2L,
                "Tierheim Regensburg",
                "tierheim@example.com",
                "Test1234!",
                Rolle.VERMITTLER
        );

        assertEquals(Rolle.VERMITTLER, nutzer.getRolle());
    }

    @Test
    void leererNameWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () ->
                new Nutzer(
                        1L, "", "max@example.com",
                        "Test1234!", Rolle.INTERESSENT
                )
        );
    }

    @Test
    void ungueltigeEmailWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () ->
                new Nutzer(
                        1L, "Max Mustermann", "ungueltig",
                        "Test1234!", Rolle.INTERESSENT
                )
        );
    }

    @Test
    void leereEmailWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () ->
                new Nutzer(
                        1L, "Max Mustermann", "",
                        "Test1234!", Rolle.INTERESSENT
                )
        );
    }

    @Test
    void zuKurzesPasswortWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () ->
                new Nutzer(
                        1L, "Max Mustermann", "max@example.com",
                        "abc", Rolle.INTERESSENT
                )
        );
    }

    @Test
    void fehlendeRolleWirdAbgelehnt() {
        assertThrows(IllegalArgumentException.class, () ->
                new Nutzer(
                        1L, "Max Mustermann", "max@example.com",
                        "Test1234!", null
                )
        );
    }

    @Test
void korrektesPasswortWirdErkannt() {
    Nutzer nutzer = new Nutzer(
            1L, "Max Mustermann", "max@example.com",
            "Test1234!", Rolle.INTERESSENT
    );

    assertTrue(nutzer.passwortPruefen("Test1234!"));
}

@Test
void falschesPasswortWirdAbgelehnt() {
    Nutzer nutzer = new Nutzer(
            1L, "Max Mustermann", "max@example.com",
            "Test1234!", Rolle.INTERESSENT
    );

    assertFalse(nutzer.passwortPruefen("Falsch123!"));
}

@Test
void fehlendesPasswortWirdAbgelehnt() {
    Nutzer nutzer = new Nutzer(
            1L, "Max Mustermann", "max@example.com",
            "Test1234!", Rolle.INTERESSENT
    );

    assertFalse(nutzer.passwortPruefen(null));
}

@Test
void neuerNutzerBesitztNochKeinProfil() {
    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    assertNull(nutzer.getInteressentenprofil());
    assertNull(nutzer.getVermittlerprofil());
}

@Test
void interessentKannInteressentenprofilErstellen() {
    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    nutzer.interessentenprofilErstellen(
            "Ich bin tierlieb.",
            "Regensburg",
            "Haus mit Garten"
    );

    assertNotNull(nutzer.getInteressentenprofil());
    assertEquals(
            "Regensburg",
            nutzer.getInteressentenprofil().getWohnort()
    );
    assertNull(nutzer.getVermittlerprofil());
}

@Test
void vermittlerKannVermittlerprofilErstellen() {
    Nutzer nutzer = new Nutzer(
            2L,
            "Tierheim Regensburg",
            "tierheim@example.com",
            "Test1234!",
            Rolle.VERMITTLER
    );

    nutzer.vermittlerprofilErstellen(
            true,
            "Tierheim Regensburg"
    );

    assertNotNull(nutzer.getVermittlerprofil());
    assertTrue(nutzer.getVermittlerprofil().isTierheim());
    assertNull(nutzer.getInteressentenprofil());
}

@Test
void interessentDarfKeinVermittlerprofilErstellen() {
    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    assertThrows(
            IllegalStateException.class,
            () -> nutzer.vermittlerprofilErstellen(false, null)
    );

    assertNull(nutzer.getVermittlerprofil());
}

@Test
void vermittlerDarfKeinInteressentenprofilErstellen() {
    Nutzer nutzer = new Nutzer(
            2L,
            "Tierheim Regensburg",
            "tierheim@example.com",
            "Test1234!",
            Rolle.VERMITTLER
    );

    assertThrows(
            IllegalStateException.class,
            () -> nutzer.interessentenprofilErstellen(
                    "Tierlieb",
                    "Regensburg",
                    "Haus"
            )
    );

    assertNull(nutzer.getInteressentenprofil());
}

@Test
void zweitesInteressentenprofilWirdAbgelehnt() {
    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    nutzer.interessentenprofilErstellen(
            "Tierlieb",
            "Regensburg",
            "Haus"
    );

    assertThrows(
            IllegalStateException.class,
            () -> nutzer.interessentenprofilErstellen(
                    "Neue Beschreibung",
                    "Muenchen",
                    "Wohnung"
            )
    );

    assertEquals(
            "Regensburg",
            nutzer.getInteressentenprofil().getWohnort()
    );
}

@Test
void zweitesVermittlerprofilWirdAbgelehnt() {
    Nutzer nutzer = new Nutzer(
            2L,
            "Tierheim Regensburg",
            "tierheim@example.com",
            "Test1234!",
            Rolle.VERMITTLER
    );

    nutzer.vermittlerprofilErstellen(
            true,
            "Tierheim Regensburg"
    );

    assertThrows(
            IllegalStateException.class,
            () -> nutzer.vermittlerprofilErstellen(
                    true,
                    "Anderes Tierheim"
            )
    );

    assertEquals(
            "Tierheim Regensburg",
            nutzer.getVermittlerprofil().getTierheimName()
    );
}

@Test
void ungueltigesProfilWirdNichtGespeichert() {
    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    assertThrows(
            IllegalArgumentException.class,
            () -> nutzer.interessentenprofilErstellen(
                    "Tierlieb",
                    "",
                    "Haus"
            )
    );

    assertNull(nutzer.getInteressentenprofil());
}

    @Test
    void nameKannGeaendertWerden() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        nutzer.datenAendern(
                "Max Mueller", "max@example.com"
        );

        assertEquals("Max Mueller", nutzer.getName());
    }

    @Test
    void emailKannGeaendertWerden() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        nutzer.datenAendern(
                "Max Mustermann", "neu@example.com"
        );

        assertEquals("neu@example.com", nutzer.getEmail());
    }

    @Test
    void nameUndEmailKoennenGemeinsamGeaendertWerden() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        nutzer.datenAendern(
                "Max Mueller", "neu@example.com"
        );

        assertEquals("Max Mueller", nutzer.getName());
        assertEquals("neu@example.com", nutzer.getEmail());
    }

    @Test
    void leererNeuerNameWirdAbgelehnt() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> nutzer.datenAendern(
                        "", "max@example.com"
                )
        );
    }

    @Test
    void ungueltigeNeueEmailWirdAbgelehnt() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> nutzer.datenAendern(
                        "Max Mustermann", "ungueltig"
                )
        );
    }

    @Test
    void nutzerIdBleibtBeiDatenaenderungErhalten() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        nutzer.datenAendern(
                "Max Mueller", "neu@example.com"
        );

        assertEquals(1L, nutzer.getId());
    }

    @Test
    void rolleBleibtBeiDatenaenderungErhalten() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        nutzer.datenAendern(
                "Max Mueller", "neu@example.com"
        );

        assertEquals(Rolle.INTERESSENT, nutzer.getRolle());
    }

    @Test
    void ungueltigeDatenaenderungErhaeltBisherigeDaten() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> nutzer.datenAendern(
                        "Max Mueller", "ungueltig"
                )
        );

        assertEquals("Max Mustermann", nutzer.getName());
        assertEquals("max@example.com", nutzer.getEmail());
    }

    @Test
    void passwortBleibtBeiDatenaenderungErhalten() {
        Nutzer nutzer = new Nutzer(
                1L, "Max Mustermann", "max@example.com",
                "Test1234!", Rolle.INTERESSENT
        );

        nutzer.datenAendern(
                "Max Mueller", "neu@example.com"
        );

        assertTrue(nutzer.passwortPruefen("Test1234!"));
    }
}