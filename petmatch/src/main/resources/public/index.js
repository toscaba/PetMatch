// Zeigt alle Tiere, die noch zu vermitteln sind, als Karten an.
async function ladeTiere() {
  const antwort = await fetch("/api/tiere/verfuegbar");
  const tiere = await antwort.json();
  const bereich = document.getElementById("tierKarten");
  bereich.innerHTML = "";

  if (tiere.length === 0) {
    bereich.textContent = "Zurzeit sind keine Tiere verfügbar.";
    return;
  }

  for (const tier of tiere) {
    const karte = document.createElement("div");
    karte.className = "karte";

    const name = document.createElement("h2");
    name.textContent = tier.name;
    karte.appendChild(name);

    for (const zeile of [
      `${tier.tierart}${tier.rasse ? ", " + tier.rasse : ""}`,
      `${tier.alter} Jahre`,
      tier.charakter,
    ]) {
      if (!zeile) continue;
      const absatz = document.createElement("p");
      absatz.textContent = zeile;
      karte.appendChild(absatz);
    }

    bereich.appendChild(karte);
  }
}

ladeTiere();
