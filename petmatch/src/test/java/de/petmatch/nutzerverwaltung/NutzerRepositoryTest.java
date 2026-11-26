package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NutzerRepositoryTest {

    @Test
    void nutzerKannGespeichertWerden() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        repository.speichern(nutzer);

        assertEquals(nutzer, repository.finden(1L));
    }

    @Test
    void unbekannteIdLiefertNull() {
        NutzerRepository repository = new NutzerRepository();

        assertNull(repository.finden(99L));
    }

    @Test
    void nutzerKannAnhandEmailGefundenWerden() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        repository.speichern(nutzer);

        assertEquals(
                nutzer,
                repository.findenNachEmail("max@example.com")
        );
    }

    @Test
    void unbekannteEmailLiefertNull() {
        NutzerRepository repository = new NutzerRepository();

        assertNull(
                repository.findenNachEmail("unbekannt@example.com")
        );
    }

    @Test
    void emailSucheIgnoriertGrossKleinschreibung() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        repository.speichern(nutzer);

        assertEquals(
                nutzer,
                repository.findenNachEmail("MAX@example.com")
        );
    }

    @Test
    void alleNutzerKoennenAbgerufenWerden() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer1 = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        Nutzer nutzer2 = new Nutzer(
                2L,
                "Tierheim Regensburg",
                "tierheim@example.com",
                "Test1234!",
                Rolle.VERMITTLER
        );

        repository.speichern(nutzer1);
        repository.speichern(nutzer2);

        assertEquals(2, repository.alle().size());
        assertTrue(repository.alle().contains(nutzer1));
        assertTrue(repository.alle().contains(nutzer2));
    }

    @Test
    void neuesRepositoryIstLeer() {
        NutzerRepository repository = new NutzerRepository();

        assertTrue(repository.alle().isEmpty());
    }

    @Test
    void idsWerdenFortlaufendVergeben() {
        NutzerRepository repository = new NutzerRepository();

        assertEquals(1L, repository.naechsteId());
        assertEquals(2L, repository.naechsteId());
        assertEquals(3L, repository.naechsteId());
    }

    @Test
    void doppelteEmailWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer1 = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        Nutzer nutzer2 = new Nutzer(
                2L,
                "Anderer Nutzer",
                "max@example.com",
                "Test5678!",
                Rolle.INTERESSENT
        );

        repository.speichern(nutzer1);

        assertThrows(
                IllegalStateException.class,
                () -> repository.speichern(nutzer2)
        );

        assertEquals(1, repository.alle().size());
    }

    @Test
    void doppelteEmailMitAndererSchreibweiseWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer1 = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        Nutzer nutzer2 = new Nutzer(
                2L,
                "Anderer Nutzer",
                "MAX@example.com",
                "Test5678!",
                Rolle.VERMITTLER
        );

        repository.speichern(nutzer1);

        assertThrows(
                IllegalStateException.class,
                () -> repository.speichern(nutzer2)
        );

        assertEquals(1, repository.alle().size());
    }

    @Test
    void doppelteIdWirdAbgelehnt() {
        NutzerRepository repository = new NutzerRepository();

        Nutzer nutzer1 = new Nutzer(
                1L,
                "Max Mustermann",
                "max@example.com",
                "Test1234!",
                Rolle.INTERESSENT
        );

        Nutzer nutzer2 = new Nutzer(
                1L,
                "Anderer Nutzer",
                "anders@example.com",
                "Test5678!",
                Rolle.VERMITTLER
        );

        repository.speichern(nutzer1);

        assertThrows(
                IllegalStateException.class,
                () -> repository.speichern(nutzer2)
        );

        assertEquals(nutzer1, repository.finden(1L));
    }

    @Test
void fehlenderNutzerWirdAbgelehnt() {
    NutzerRepository repository = new NutzerRepository();

    assertThrows(
            IllegalArgumentException.class,
            () -> repository.speichern(null)
    );

    assertTrue(repository.alle().isEmpty());
}

@Test
void nutzerOhneIdWirdAbgelehnt() {
    NutzerRepository repository = new NutzerRepository();

    Nutzer nutzer = new Nutzer(
            null,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    assertThrows(
            IllegalArgumentException.class,
            () -> repository.speichern(nutzer)
    );

    assertTrue(repository.alle().isEmpty());
}

@Test
void fehlendeEmailBeiSucheLiefertNull() {
    NutzerRepository repository = new NutzerRepository();

    assertNull(repository.findenNachEmail(null));
}

@Test
void bereitsBelegteIdWirdUebersprungen() {
    NutzerRepository repository = new NutzerRepository();

    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    repository.speichern(nutzer);

    assertEquals(2L, repository.naechsteId());
}

@Test
void zurueckgegebeneListeVeraendertRepositoryNicht() {
    NutzerRepository repository = new NutzerRepository();

    Nutzer nutzer = new Nutzer(
            1L,
            "Max Mustermann",
            "max@example.com",
            "Test1234!",
            Rolle.INTERESSENT
    );

    repository.speichern(nutzer);

    repository.alle().clear();

    assertEquals(1, repository.alle().size());
    assertEquals(nutzer, repository.finden(1L));
}
}