package de.petmatch.nutzerverwaltung;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class NutzerRepository {

    private final Map<Long, Nutzer> nutzer = new LinkedHashMap<>();
    private long naechsteId = 1L;

    public synchronized Long naechsteId() {

        while (nutzer.containsKey(naechsteId)) {
            naechsteId++;
        }

        return naechsteId++;
    }

    public synchronized void speichern(Nutzer neuerNutzer) {

        if (neuerNutzer == null || neuerNutzer.getId() == null) {
            throw new IllegalArgumentException(
                    "Ein Nutzer mit gueltiger ID ist erforderlich."
            );
        }

        if (nutzer.containsKey(neuerNutzer.getId())) {
            throw new IllegalStateException(
                    "Diese Nutzer-ID existiert bereits."
            );
        }

        if (findenNachEmail(neuerNutzer.getEmail()) != null) {
            throw new IllegalStateException(
                    "Diese E-Mail-Adresse ist bereits registriert."
            );
        }

        nutzer.put(neuerNutzer.getId(), neuerNutzer);
    }

    public synchronized Nutzer finden(Long id) {
        return nutzer.get(id);
    }

    public synchronized Nutzer findenNachEmail(String email) {

        if (email == null) {
            return null;
        }

        for (Nutzer gespeicherterNutzer : nutzer.values()) {

            if (gespeicherterNutzer.getEmail().equalsIgnoreCase(email)) {
                return gespeicherterNutzer;
            }
        }

        return null;
    }

    public synchronized List<Nutzer> alle() {
        return new ArrayList<>(nutzer.values());
    }

    public synchronized void datenAendern(
            Long id,
            String name,
            String email) {

        Nutzer nutzer = finden(id);

        if (nutzer == null) {
            throw new IllegalArgumentException(
                    "Nutzer nicht gefunden."
            );
        }

        Nutzer vorhandenerNutzer = findenNachEmail(email);

        if (vorhandenerNutzer != null
                && !vorhandenerNutzer.getId().equals(id)) {

            throw new IllegalStateException(
                    "Diese E-Mail-Adresse ist bereits registriert."
            );
        }

        nutzer.datenAendern(name, email);
    }
}