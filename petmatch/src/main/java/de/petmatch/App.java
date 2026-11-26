package de.petmatch;

import de.petmatch.tierverwaltung.Tier;
import de.petmatch.tierverwaltung.TierRepository;
import de.petmatch.tierverwaltung.TierVerwaltung;

import de.petmatch.nutzerverwaltung.Nutzer;
import de.petmatch.nutzerverwaltung.NutzerRepository;
import de.petmatch.nutzerverwaltung.NutzerVerwaltung;
import de.petmatch.nutzerverwaltung.Rolle;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.staticfiles.Location;

import java.util.function.Consumer;
import java.util.Map;

public class App {

    // Daten aus dem Formular für ein neues Tier
    record TierDaten(
        String name,
        String tierart,
        String rasse,
        int alter,
        String charakter
    ) {}

    // Daten für die Registrierung
    record RegistrierungsDaten(
        String name,
        String email,
        String passwort,
        Rolle rolle
    ) {}

    // Daten für die Anmeldung
    record AnmeldeDaten(
        String email,
        String passwort
    ) {}

    // Daten für die Bearbeitung eines Nutzers
    record NutzerDaten(
        String name,
        String email
    ) {}

    // Daten für das Interessentenprofil
    record InteressentenprofilDaten(
        String ueberMich,
        String wohnort,
        String wohnsituation
    ) {}

    // Daten für das Vermittlerprofil
    record VermittlerprofilDaten(
        boolean istTierheim,
        String tierheimName
    ) {}

    // Antwortobjekt ohne Passwort oder Passwort-Hash
    record NutzerAntwort(
        Long id,
        String name,
        String email,
        Rolle rolle,
        de.petmatch.nutzerverwaltung.Interessentenprofil interessentenprofil,
        de.petmatch.nutzerverwaltung.Vermittlerprofil vermittlerprofil
    ) {}

    // Repositories und Nutzerverwaltung
    private static final TierRepository repository =
        new TierRepository();

    private static final NutzerRepository nutzerRepository =
        new NutzerRepository();

    private static final NutzerVerwaltung nutzerVerwaltung =
        new NutzerVerwaltung(nutzerRepository);

    public static void main(String[] args) {

        beispieldatenAnlegen();

        Javalin app = Javalin.create(config ->
            config.staticFiles.add("/public", Location.CLASSPATH)
        );

        tierRoutenRegistrieren(app);
        nutzerRoutenRegistrieren(app);

        // Fehlerbehandlung
        app.exception(IllegalArgumentException.class,
            (e, ctx) -> ctx.status(400).result(e.getMessage()));

        app.exception(IllegalStateException.class,
            (e, ctx) -> ctx.status(409).result(e.getMessage()));

        app.exception(SecurityException.class,
            (e, ctx) -> ctx.status(401).result(e.getMessage()));

        app.start(7070);
    }

    // ==========================================
    // TIERVERWALTUNG
    // ==========================================

    private static void tierRoutenRegistrieren(Javalin app) {

        app.get("/api/tiere", ctx ->
            ctx.json(repository.alle())
        );

        // Nur Tiere, die noch zu vermitteln sind
        app.get("/api/tiere/verfuegbar", ctx ->
            ctx.json(
                new TierVerwaltung(
                    repository.alle()
                ).getVerfuegbareTiere()
            )
        );

        app.post("/api/tiere", ctx -> {

            TierDaten d = ctx.bodyAsClass(TierDaten.class);

            Tier tier = new Tier(
                repository.naechsteId(),
                1L,
                d.name(),
                d.tierart(),
                d.rasse(),
                d.alter(),
                d.charakter(),
                null
            );

            repository.speichern(tier);

            ctx.status(201).json(tier);
        });

        app.post("/api/tiere/{id}/reservieren",
            ctx -> aktion(ctx, Tier::reservieren));

        app.post("/api/tiere/{id}/freigeben",
            ctx -> aktion(ctx, Tier::freigeben));

        app.post("/api/tiere/{id}/adoptieren",
            ctx -> aktion(ctx, Tier::adoptieren));

        app.post("/api/tiere/{id}/loeschen",
            ctx -> aktion(ctx, Tier::loeschen));
    }

    // ==========================================
    // NUTZERVERWALTUNG
    // ==========================================

    private static void nutzerRoutenRegistrieren(Javalin app) {

        // Registrierung
        app.post("/api/nutzer/registrieren", ctx -> {

            RegistrierungsDaten d =
                ctx.bodyAsClass(RegistrierungsDaten.class);

            Nutzer nutzer = nutzerVerwaltung.registrieren(
                d.name(),
                d.email(),
                d.passwort(),
                d.rolle()
            );

            ctx.status(201).json(nutzerAntwort(nutzer));
        });

        // Anmeldung
        app.post("/api/nutzer/anmelden", ctx -> {

            AnmeldeDaten d =
                ctx.bodyAsClass(AnmeldeDaten.class);

            String token;

            try {
                token = nutzerVerwaltung.anmelden(
                    d.email(),
                    d.passwort()
                );
            } catch (IllegalArgumentException e) {
                ctx.status(401).result(e.getMessage());
                return;
            }

            Nutzer nutzer =
                nutzerVerwaltung.angemeldetenNutzerErmitteln(token);

            ctx.json(Map.of(
                "token", token,
                "nutzer", nutzerAntwort(nutzer)
            ));
        });

        // Abmeldung
        app.post("/api/nutzer/abmelden", ctx -> {

            nutzerVerwaltung.abmelden(tokenErmitteln(ctx));

            ctx.status(204);
        });

        // Eigene Nutzerdaten abrufen
        app.get("/api/nutzer/me", ctx -> {

            Nutzer nutzer = angemeldetenNutzerPruefen(ctx);

            ctx.json(nutzerAntwort(nutzer));
        });

        // Eigene Nutzerdaten bearbeiten
        app.put("/api/nutzer/me", ctx -> {

            NutzerDaten d =
                ctx.bodyAsClass(NutzerDaten.class);

            Nutzer nutzer = nutzerVerwaltung.datenAendern(
                tokenErmitteln(ctx),
                d.name(),
                d.email()
            );

            ctx.json(nutzerAntwort(nutzer));
        });

        // Interessentenprofil bearbeiten
        app.put("/api/nutzer/me/interessentenprofil", ctx -> {

            Nutzer nutzer = angemeldetenNutzerPruefen(ctx);

            if (nutzer.getRolle() != Rolle.INTERESSENT) {
                ctx.status(403).result(
                    "Nur Interessenten duerfen dieses Profil bearbeiten."
                );
                return;
            }

            InteressentenprofilDaten d =
                ctx.bodyAsClass(InteressentenprofilDaten.class);

            Nutzer aktualisierterNutzer =
                nutzerVerwaltung.interessentenprofilBearbeiten(
                    tokenErmitteln(ctx),
                    d.ueberMich(),
                    d.wohnort(),
                    d.wohnsituation()
                );

            ctx.json(nutzerAntwort(aktualisierterNutzer));
        });

        // Vermittlerprofil bearbeiten
        app.put("/api/nutzer/me/vermittlerprofil", ctx -> {

            Nutzer nutzer = angemeldetenNutzerPruefen(ctx);

            if (nutzer.getRolle() != Rolle.VERMITTLER) {
                ctx.status(403).result(
                    "Nur Vermittler duerfen dieses Profil bearbeiten."
                );
                return;
            }

            VermittlerprofilDaten d =
                ctx.bodyAsClass(VermittlerprofilDaten.class);

            Nutzer aktualisierterNutzer =
                nutzerVerwaltung.vermittlerprofilBearbeiten(
                    tokenErmitteln(ctx),
                    d.istTierheim(),
                    d.tierheimName()
                );

            ctx.json(nutzerAntwort(aktualisierterNutzer));
        });
    }

    // ==========================================
    // HILFSMETHODEN
    // ==========================================

    // Sitzungstoken aus dem HTTP-Header auslesen
    private static String tokenErmitteln(Context ctx) {

        String authorization = ctx.header("Authorization");

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {
            return null;
        }

        return authorization.substring(7).trim();
    }

    // Prüfen, ob ein Nutzer angemeldet ist
    private static Nutzer angemeldetenNutzerPruefen(Context ctx) {

        Nutzer nutzer =
            nutzerVerwaltung.angemeldetenNutzerErmitteln(
                tokenErmitteln(ctx)
            );

        if (nutzer == null) {
            throw new SecurityException(
                "Anmeldung erforderlich."
            );
        }

        return nutzer;
    }

    // Nur freigegebene Nutzerdaten an das Frontend senden
    private static NutzerAntwort nutzerAntwort(Nutzer nutzer) {

        return new NutzerAntwort(
            nutzer.getId(),
            nutzer.getName(),
            nutzer.getEmail(),
            nutzer.getRolle(),
            nutzer.getInteressentenprofil(),
            nutzer.getVermittlerprofil()
        );
    }

    // Sucht das Tier zur ID und führt die Aktion aus
    private static void aktion(
        Context ctx,
        Consumer<Tier> aktion
    ) {

        long id = Long.parseLong(ctx.pathParam("id"));

        Tier tier = repository.finden(id);

        if (tier == null) {
            throw new NotFoundResponse("Tier nicht gefunden.");
        }

        aktion.accept(tier);

        ctx.json(tier);
    }

    // Beispieldaten für die Tierverwaltung
    private static void beispieldatenAnlegen() {

        repository.speichern(
            new Tier(
                repository.naechsteId(),
                1L,
                "Bello",
                "Hund",
                "Labrador",
                3,
                "Freundlich",
                null
            )
        );

        repository.speichern(
            new Tier(
                repository.naechsteId(),
                1L,
                "Mimi",
                "Katze",
                "Europäisch Kurzhaar",
                2,
                "Ruhig",
                null
            )
        );
    }
}