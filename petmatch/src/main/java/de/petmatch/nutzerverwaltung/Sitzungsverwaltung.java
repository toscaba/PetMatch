package de.petmatch.nutzerverwaltung;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

public class Sitzungsverwaltung {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final Map<String, Long> sitzungen = new HashMap<>();

    public synchronized String sitzungErstellen(Long nutzerId) {

        if (nutzerId == null) {
            throw new IllegalArgumentException(
                    "Eine Nutzer-ID ist erforderlich."
            );
        }

        String token;

        do {
            token = tokenErzeugen();
        } while (sitzungen.containsKey(token));

        sitzungen.put(token, nutzerId);

        return token;
    }

    public synchronized Long nutzerErmitteln(String token) {

        if (token == null) {
            return null;
        }

        return sitzungen.get(token);
    }

    public synchronized void sitzungBeenden(String token) {

        if (token != null) {
            sitzungen.remove(token);
        }
    }

    private String tokenErzeugen() {

        byte[] zufallswerte = new byte[32];

        RANDOM.nextBytes(zufallswerte);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(zufallswerte);
    }
}