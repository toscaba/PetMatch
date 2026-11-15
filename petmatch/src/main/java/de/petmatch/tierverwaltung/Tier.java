
package de.petmatch.tierverwaltung;

public class Tier {

    // Attribute entsprechend dem Domänenmodell
    private final Long id;
    private final Long nutzerId;
    private String name;
    private String tierart;
    private String rasse;
    private int alter;
    private String charakter;
    private String foto;

    private TierStatus status;

    // Konstruktor: Erstellt ein neues Tierprofil
    public Tier(Long id, Long nutzerId, String name,
                String tierart, String rasse, int alter,
                String charakter, String foto) {

        pruefeEingaben(name, tierart, alter);

        // Attribute zuweisen
        this.id = id;
        this.nutzerId = nutzerId;
        this.name = name;
        this.tierart = tierart;
        this.rasse = rasse;
        this.alter = alter;
        this.charakter = charakter;
        this.foto = foto;

        // Jedes neue Tier ist zunächst verfügbar
        this.status = TierStatus.ZU_VERMITTELN;
    }

    // Validierung der Eingaben, genutzt von Konstruktor und aendern()
    private static void pruefeEingaben(String name, String tierart, int alter) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                "Der Tiername darf nicht leer sein."
            );
        }

        if (tierart == null || tierart.isBlank()) {
            throw new IllegalArgumentException(
                "Die Tierart darf nicht leer sein."
            );
        }

        if (alter < 0) {
            throw new IllegalArgumentException(
                "Das Alter darf nicht negativ sein."
            );
        }
    }

    // Getter für die JUnit-Tests
    public Long getId() {
        return id;
    }

    public Long getNutzerId() {
        return nutzerId;
    }

    public String getName() {
        return name;
    }

    public String getTierart() {
        return tierart;
    }

    public String getRasse() {
        return rasse;
    }

    public int getAlter() {
        return alter;
    }

    public String getCharakter() {
        return charakter;
    }

    public String getFoto() {
        return foto;
    }

    public TierStatus getStatus() {
        return status;
    }


    // Reserviert ein Tier, wenn es noch verfügbar ist.
    public void reservieren() {

        if (status != TierStatus.ZU_VERMITTELN) {
            throw new IllegalStateException(
                "Das Tier ist nicht verfügbar."
            );
        }

        status = TierStatus.RESERVIERT;
    }


    // Gibt ein reserviertes Tier wieder zur Vermittlung frei,
    // wenn es nicht adoptiert wurde.
    public void freigeben() {

        if (status != TierStatus.RESERVIERT) {
            throw new IllegalStateException(
                "Nur reservierte Tiere können wieder freigegeben werden."
            );
        }

        status = TierStatus.ZU_VERMITTELN;
    }


    // Ein reserviertes Tier wird adoptiert und ist damit vermittelt.
    public void adoptieren() {

        if (status != TierStatus.RESERVIERT) {
            throw new IllegalStateException(
                "Nur reservierte Tiere können adoptiert werden."
            );
        }

        status = TierStatus.VERMITTELT;
    }


    // Löscht das Inserat. Ein gelöschtes Tier kann nicht erneut gelöscht werden.
    public void loeschen() {

        if (status == TierStatus.GELOESCHT) {
            throw new IllegalStateException(
                "Das Tier ist bereits gelöscht."
            );
        }

        status = TierStatus.GELOESCHT;
    }


    // Ändert das Tierprofil. Es gelten dieselben Regeln wie beim Erstellen.
    public void aendern(String name, String tierart, String rasse,
                        int alter, String charakter, String foto) {

        pruefeEingaben(name, tierart, alter);

        this.name = name;
        this.tierart = tierart;
        this.rasse = rasse;
        this.alter = alter;
        this.charakter = charakter;
        this.foto = foto;
    }


}
