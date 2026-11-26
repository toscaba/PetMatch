package de.petmatch.nutzerverwaltung;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

final class PasswortHasher {

    private static final String ALGORITHMUS =
            "PBKDF2WithHmacSHA256";

    private static final int ITERATIONEN = 600_000;
    private static final int SALT_LAENGE = 16;
    private static final int HASH_LAENGE = 256;

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private PasswortHasher() {
    }

    static String hashen(String passwort) {

        if (passwort == null) {
            throw new IllegalArgumentException(
                    "Das Passwort darf nicht null sein."
            );
        }

        byte[] salt = new byte[SALT_LAENGE];
        RANDOM.nextBytes(salt);

        byte[] hash = berechneHash(passwort, salt);

        return ALGORITHMUS + "$"
                + ITERATIONEN + "$"
                + Base64.getEncoder().encodeToString(salt)
                + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    static boolean pruefen(String passwort,
                           String gespeicherterHash) {

        if (passwort == null || gespeicherterHash == null) {
            return false;
        }

        String[] teile = gespeicherterHash.split("\\$", -1);

        if (teile.length != 4
                || !ALGORITHMUS.equals(teile[0])
                || !String.valueOf(ITERATIONEN).equals(teile[1])) {
            return false;
        }

        try {
            byte[] salt = Base64.getDecoder().decode(teile[2]);
            byte[] alterHash = Base64.getDecoder().decode(teile[3]);

            if (salt.length != SALT_LAENGE
                    || alterHash.length != HASH_LAENGE / 8) {
                return false;
            }

            byte[] neuerHash = berechneHash(passwort, salt);

            return MessageDigest.isEqual(alterHash, neuerHash);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] berechneHash(String passwort,
                                       byte[] salt) {

        PBEKeySpec spec = new PBEKeySpec(
                passwort.toCharArray(),
                salt,
                ITERATIONEN,
                HASH_LAENGE
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHMUS);

            return factory.generateSecret(spec).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Passwort konnte nicht verarbeitet werden.", e
            );
        } finally {
            spec.clearPassword();
        }
    }
}
