package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NutzerProfilverwaltungTest {

    @Test
    void angemeldeterNutzerKannDatenAendern() {

        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com", "Test1234!"
        );

        verwaltung.datenAendern(
                token, "Max Mueller", "neu@example.com"
        );

        assertEquals("Max Mueller", nutzer.getName());
        assertEquals("neu@example.com", nutzer.getEmail());

        assertNull(repository.findenNachEmail("max@example.com"));
        assertEquals(nutzer, repository.findenNachEmail("neu@example.com"));

        assertNotNull(
                verwaltung.anmelden("neu@example.com", "Test1234!")
        );
    }

    @Test
    void datenaenderungOhneAnmeldungWirdAbgelehnt() {

        NutzerVerwaltung verwaltung =
                new NutzerVerwaltung(new NutzerRepository());

        assertThrows(
                SecurityException.class,
                () -> verwaltung.datenAendern(
                        null, "Max Mueller", "neu@example.com"
                )
        );
    }

    @Test
    void bereitsVergebeneEmailWirdAbgelehnt() {

        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        verwaltung.registrieren(
                "Anna Musterfrau",
                "anna@example.com",
                "Test5678!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com", "Test1234!"
        );

        assertThrows(
                IllegalStateException.class,
                () -> verwaltung.datenAendern(
                        token, "Max Mueller", "ANNA@EXAMPLE.COM"
                )
        );

        assertEquals("Max Mustermann", nutzer.getName());
        assertEquals("max@example.com", nutzer.getEmail());
    }

    @Test
    void interessentKannProfilAnlegenUndBearbeiten() {

        NutzerVerwaltung verwaltung =
                new NutzerVerwaltung(new NutzerRepository());

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com", "Test1234!"
        );

        verwaltung.interessentenprofilBearbeiten(
                token,
                "Ich liebe Tiere.",
                "Regensburg",
                "Wohnung"
        );

        assertNotNull(nutzer.getInteressentenprofil());

        verwaltung.interessentenprofilBearbeiten(
                token,
                "Ich suche eine Katze.",
                "Kelheim",
                "Haus mit Garten"
        );

        assertEquals(
                "Kelheim",
                nutzer.getInteressentenprofil().getWohnort()
        );

        assertEquals(
                "Haus mit Garten",
                nutzer.getInteressentenprofil().getWohnsituation()
        );
    }

    @Test
    void vermittlerKannProfilAnlegenUndBearbeiten() {

        NutzerVerwaltung verwaltung =
                new NutzerVerwaltung(new NutzerRepository());

        Nutzer nutzer = verwaltung.registrieren(
                "Tierheim",
                "tierheim@example.com",
                "Test1234!",
                Rolle.VERMITTLER
        );

        String token = verwaltung.anmelden(
                "tierheim@example.com", "Test1234!"
        );

        verwaltung.vermittlerprofilBearbeiten(
                token, true, "Tierheim Regensburg"
        );

        assertNotNull(nutzer.getVermittlerprofil());

        verwaltung.vermittlerprofilBearbeiten(
                token, true, "Tierheim Kelheim"
        );

        assertEquals(
                "Tierheim Kelheim",
                nutzer.getVermittlerprofil().getTierheimName()
        );
    }

    @Test
    void interessentDarfKeinVermittlerprofilBearbeiten() {

        NutzerVerwaltung verwaltung =
                new NutzerVerwaltung(new NutzerRepository());

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com", "Test1234!"
        );

        assertThrows(
                IllegalStateException.class,
                () -> verwaltung.vermittlerprofilBearbeiten(
                        token, true, "Tierheim Regensburg"
                )
        );
    }

    @Test
    void profilbearbeitungOhneAnmeldungWirdAbgelehnt() {

        NutzerVerwaltung verwaltung =
                new NutzerVerwaltung(new NutzerRepository());

        assertThrows(
                SecurityException.class,
                () -> verwaltung.interessentenprofilBearbeiten(
                        null, "Tierfreund", "Regensburg", "Wohnung"
                )
        );

        assertThrows(
                SecurityException.class,
                () -> verwaltung.vermittlerprofilBearbeiten(
                        null, true, "Tierheim Regensburg"
                )
        );
    }
}