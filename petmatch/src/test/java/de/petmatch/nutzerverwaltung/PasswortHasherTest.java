package de.petmatch.nutzerverwaltung;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswortHasherTest {

    @Test
    void passwortWirdNichtImKlartextGespeichert() {
        String passwort = "Test1234!";
        String hash = PasswortHasher.hashen(passwort);

        assertNotNull(hash);
        assertFalse(hash.isBlank());
        assertFalse(hash.contains(passwort));
    }

    @Test
    void gleichePasswoerterErzeugenUnterschiedlicheHashes() {
        String passwort = "Test1234!";

        String hash1 = PasswortHasher.hashen(passwort);
        String hash2 = PasswortHasher.hashen(passwort);

        assertNotEquals(hash1, hash2);
    }

    @Test
    void korrektesPasswortWirdVerifiziert() {
        String hash = PasswortHasher.hashen("Test1234!");

        assertTrue(
                PasswortHasher.pruefen("Test1234!", hash)
        );
    }

    @Test
    void falschesPasswortWirdNichtVerifiziert() {
        String hash = PasswortHasher.hashen("Test1234!");

        assertFalse(
                PasswortHasher.pruefen("Falsch123!", hash)
        );
    }

    @Test
    void nullPasswortWirdAbgelehnt() {
        String hash = PasswortHasher.hashen("Test1234!");

        assertFalse(
                PasswortHasher.pruefen(null, hash)
        );
    }

    @Test
    void ungueltigerHashWirdAbgelehnt() {
        assertFalse(
                PasswortHasher.pruefen(
                        "Test1234!", "ungueltigerHash"
                )
        );
    }

    @Test
void nullPasswortKannNichtGehashtWerden() {
    assertThrows(
            IllegalArgumentException.class,
            () -> PasswortHasher.hashen(null)
    );
}

@Test
void fehlenderHashWirdAbgelehnt() {
    assertFalse(
            PasswortHasher.pruefen("Test1234!", null)
    );
}

@Test
void beschaedigterHashWirdAbgelehnt() {
    String hash = PasswortHasher.hashen("Test1234!");
    String beschaedigterHash = hash + "!";

    assertFalse(
            PasswortHasher.pruefen(
                    "Test1234!", beschaedigterHash
            )
    );
}
}