package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NutzerVerwaltungTest {

    @Test
    void interessentKannSichRegistrieren() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertNotNull(nutzer);
        assertEquals(1L, nutzer.getId());
        assertEquals("Max Mustermann", nutzer.getName());
        assertEquals("max@example.com", nutzer.getEmail());
        assertEquals(Rolle.INTERESSENT, nutzer.getRolle());
    }

    @Test
    void vermittlerKannSichRegistrieren() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Tierheim Regensburg",
                "tierheim@example.com",
                "Test1234!",
                Rolle.VERMITTLER
        );

        assertNotNull(nutzer);
        assertEquals(Rolle.VERMITTLER, nutzer.getRolle());
    }

    @Test
    void registrierterNutzerWirdGespeichert() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertEquals(nutzer, repository.finden(nutzer.getId()));
        assertEquals(1, repository.alle().size());
    }

    @Test
    void registrierteNutzerErhaltenUnterschiedlicheIds() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer1 = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        Nutzer nutzer2 = verwaltung.registrieren(
                "Anna Musterfrau",
                "anna@example.com",
                "Test5678!",
                Rolle.INTERESSENT
        );

        assertNotEquals(nutzer1.getId(), nutzer2.getId());
        assertEquals(2, repository.alle().size());
    }

    @Test
    void registrierungMitUngueltigerEmailWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.registrieren(
                        "Max Mustermann",
                        "ungueltig",
                        "Test1234!",
                        Rolle.INTERESSENT
                )
        );

        assertTrue(repository.alle().isEmpty());
    }

    @Test
    void registrierungMitZuKurzemPasswortWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.registrieren(
                        "Max Mustermann",
                        "max@example.com",
                        "abc",
                        Rolle.INTERESSENT
                )
        );

        assertTrue(repository.alle().isEmpty());
    }

    @Test
    void doppelteRegistrierungWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertThrows(
                IllegalStateException.class,
                () -> verwaltung.registrieren(
                        "Anderer Nutzer",
                        "max@example.com",
                        "Test5678!",
                        Rolle.VERMITTLER
                )
        );

        assertEquals(1, repository.alle().size());
    }

    @Test
    void registriertesPasswortKannGeprueftWerden() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertTrue(nutzer.passwortPruefen("Test1234!"));
        assertFalse(nutzer.passwortPruefen("Falsch123!"));
    }

        @Test
    void registrierterNutzerKannSichAnmelden() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com",
                "Test1234!"
        );

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void anmeldungMitFalschemPasswortWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden(
                        "max@example.com",
                        "Falsch123!"
                )
        );
    }

    @Test
    void anmeldungMitUnbekannterEmailWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden(
                        "unbekannt@example.com",
                        "Test1234!"
                )
        );
    }

    @Test
    void anmeldungOhneEmailWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden(null, "Test1234!")
        );
    }

    @Test
    void anmeldungOhnePasswortWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden("max@example.com", null)
        );
    }

    @Test
    void anmeldungIgnoriertGrossKleinschreibungDerEmail() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "MAX@EXAMPLE.COM",
                "Test1234!"
        );

        assertNotNull(token);
    }

    @Test
    void angemeldeterNutzerKannErmitteltWerden() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com",
                "Test1234!"
        );

        assertEquals(
                nutzer,
                verwaltung.angemeldetenNutzerErmitteln(token)
        );
    }

    @Test
    void nutzerKannSichAbmelden() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token = verwaltung.anmelden(
                "max@example.com",
                "Test1234!"
        );

        verwaltung.abmelden(token);

        assertNull(
                verwaltung.angemeldetenNutzerErmitteln(token)
        );
    }

    @Test
    void ungueltigesTokenLiefertKeinenNutzer() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertNull(
                verwaltung.angemeldetenNutzerErmitteln(
                        "ungueltiges-token"
                )
        );
    }

    @Test
    void nutzerKannSichNachAbmeldungErneutAnmelden() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token1 = verwaltung.anmelden(
                "max@example.com",
                "Test1234!"
        );

        verwaltung.abmelden(token1);

        String token2 = verwaltung.anmelden(
                "max@example.com",
                "Test1234!"
        );

        assertNotEquals(token1, token2);

        assertNull(
                verwaltung.angemeldetenNutzerErmitteln(token1)
        );

        assertEquals(
                nutzer,
                verwaltung.angemeldetenNutzerErmitteln(token2)
        );
    }

        @Test
    void ungueltigeZugangsdatenErzeugenGleicheFehlermeldung() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        IllegalArgumentException falschesPasswort = assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden(
                        "max@example.com", "Falsch123!"
                )
        );

        IllegalArgumentException unbekannteEmail = assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden(
                        "unbekannt@example.com", "Test1234!"
                )
        );

        assertEquals(
                falschesPasswort.getMessage(),
                unbekannteEmail.getMessage()
        );
    }

    @Test
    void anmeldungMitLeererEmailWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden("", "Test1234!")
        );
    }

    @Test
    void anmeldungMitLeeremPasswortWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> verwaltung.anmelden("max@example.com", "")
        );
    }

    @Test
    void fehlendesTokenLiefertKeinenNutzer() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertNull(
                verwaltung.angemeldetenNutzerErmitteln(null)
        );
    }

    @Test
    void abmeldungOhneTokenVerursachtKeinenFehler() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        assertDoesNotThrow(
                () -> verwaltung.abmelden(null)
        );
    }

    @Test
    void mehrereAnmeldungenSindUnabhaengig() {
        NutzerRepository repository = new NutzerRepository();
        NutzerVerwaltung verwaltung = new NutzerVerwaltung(repository);

        Nutzer nutzer = verwaltung.registrieren(
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        String token1 = verwaltung.anmelden(
                "max@example.com", "Test1234!"
        );

        String token2 = verwaltung.anmelden(
                "max@example.com", "Test1234!"
        );

        assertNotEquals(token1, token2);

        assertEquals(
                nutzer,
                verwaltung.angemeldetenNutzerErmitteln(token1)
        );

        assertEquals(
                nutzer,
                verwaltung.angemeldetenNutzerErmitteln(token2)
        );

        verwaltung.abmelden(token1);

        assertNull(
                verwaltung.angemeldetenNutzerErmitteln(token1)
        );

        assertEquals(
                nutzer,
                verwaltung.angemeldetenNutzerErmitteln(token2)
        );
    }
}