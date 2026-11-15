package de.petmatch;

import de.petmatch.tierverwaltung.Tier;
import de.petmatch.tierverwaltung.TierRepository;
import de.petmatch.tierverwaltung.TierVerwaltung;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.staticfiles.Location;

import java.util.function.Consumer;

public class App {

    // Daten aus dem Formular für ein neues Tier
    record TierDaten(String name, String tierart, String rasse,
                     int alter, String charakter) {}

    private static final TierRepository repository = new TierRepository();

    public static void main(String[] args) {

        beispieldatenAnlegen();

        Javalin app = Javalin.create(config ->
            config.staticFiles.add("/public", Location.CLASSPATH)
        );

        app.get("/api/tiere", ctx -> ctx.json(repository.alle()));

        // Nur Tiere, die noch zu vermitteln sind (Startseite für Interessenten)
        app.get("/api/tiere/verfuegbar", ctx -> ctx.json(
            new TierVerwaltung(repository.alle()).getVerfuegbareTiere()
        ));

        app.post("/api/tiere", ctx -> {
            TierDaten d = ctx.bodyAsClass(TierDaten.class);
            Tier tier = new Tier(
                repository.naechsteId(), 1L, d.name(), d.tierart(),
                d.rasse(), d.alter(), d.charakter(), null
            );
            repository.speichern(tier);
            ctx.status(201).json(tier);
        });

        app.post("/api/tiere/{id}/reservieren", ctx -> aktion(ctx, Tier::reservieren));
        app.post("/api/tiere/{id}/freigeben", ctx -> aktion(ctx, Tier::freigeben));
        app.post("/api/tiere/{id}/adoptieren", ctx -> aktion(ctx, Tier::adoptieren));
        app.post("/api/tiere/{id}/loeschen", ctx -> aktion(ctx, Tier::loeschen));

        // Fehler aus Tier als HTTP-Antwort mit Meldung zurückgeben
        app.exception(IllegalArgumentException.class,
            (e, ctx) -> ctx.status(400).result(e.getMessage()));
        app.exception(IllegalStateException.class,
            (e, ctx) -> ctx.status(409).result(e.getMessage()));

        app.start(7070);
    }

    // Sucht das Tier zur ID und führt die Aktion aus.
    private static void aktion(Context ctx, Consumer<Tier> aktion) {
        long id = Long.parseLong(ctx.pathParam("id"));
        Tier tier = repository.finden(id);
        if (tier == null) {
            throw new NotFoundResponse("Tier nicht gefunden.");
        }
        aktion.accept(tier);
        ctx.json(tier);
    }

    private static void beispieldatenAnlegen() {
        repository.speichern(new Tier(repository.naechsteId(), 1L,
            "Bello", "Hund", "Labrador", 3, "Freundlich", null));
        repository.speichern(new Tier(repository.naechsteId(), 1L,
            "Mimi", "Katze", "Europäisch Kurzhaar", 2, "Ruhig", null));
    }
}
