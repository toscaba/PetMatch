package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InteressentenprofilTest {

    @Test
    void gueltigesProfilKannErstelltWerden() {
        Interessentenprofil profil = new Interessentenprofil(
                "Ich bin tierlieb und gerne in der Natur.",
                "Regensburg",
                "Haus mit Garten"
        );

        assertEquals(
                "Ich bin tierlieb und gerne in der Natur.",
                profil.getUeberMich()
        );

        assertEquals(
                "Regensburg",
                profil.getWohnort()
        );

        assertEquals(
                "Haus mit Garten",
                profil.getWohnsituation()
        );
    }

    @Test
    void leererWohnortWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Interessentenprofil(
                        "Ich bin tierlieb.",
                        "",
                        "Haus mit Garten"
                )
        );
    }

    @Test
    void fehlenderWohnortWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Interessentenprofil(
                        "Ich bin tierlieb.",
                        null,
                        "Haus mit Garten"
                )
        );
    }

    @Test
    void leereWohnsituationWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Interessentenprofil(
                        "Ich bin tierlieb.",
                        "Regensburg",
                        ""
                )
        );
    }

    @Test
    void fehlendeWohnsituationWirdAbgelehnt() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Interessentenprofil(
                        "Ich bin tierlieb.",
                        "Regensburg",
                        null
                )
        );
    }

    @Test
    void profilOhneBeschreibungKannErstelltWerden() {
        Interessentenprofil profil = new Interessentenprofil(
                "",
                "Regensburg",
                "Wohnung"
        );

        assertEquals("", profil.getUeberMich());
    }
}