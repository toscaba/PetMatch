// Hier merken wir uns alle Tiere, damit wir nicht bei jedem Tastendruck neu laden müssen.
let alleTiere = [];

// 1. Tiere einmal vom Server holen
async function ladeTiere() {
  const antwort = await fetch("/api/tiere/verfuegbar");
  alleTiere = await antwort.json();
  fuelleTierartAuswahl();
  filtern();
}

// Baut die Auswahl "Hund, Katze, ..." aus den vorhandenen Tieren
function fuelleTierartAuswahl() {
  const auswahl = document.getElementById("tierartFilter");
  const tierarten = [...new Set(alleTiere.map((tier) => tier.tierart))].sort();

  for (const art of tierarten) {
    const option = document.createElement("option");
    option.value = art;
    option.textContent = art;
    auswahl.appendChild(option);
  }
}

// 2. Das "Sieb": nur Tiere behalten, die zur Suche passen
function filtern() {
  const suchtext = document.getElementById("suchfeld").value.toLowerCase();
  const tierart = document.getElementById("tierartFilter").value;

  const gefiltert = alleTiere.filter((tier) => {
    const text = `${tier.name} ${tier.rasse} ${tier.charakter}`.toLowerCase();
    const passtText = text.includes(suchtext);
    const passtArt = tierart === "" || tier.tierart === tierart;
    return passtText && passtArt;
  });

  zeigeTiere(gefiltert);
}

// 3. Die passenden Tiere als Karten anzeigen
function zeigeTiere(tiere) {
  const bereich = document.getElementById("tierKarten");
  bereich.innerHTML = "";

  if (alleTiere.length === 0) {
    bereich.textContent = "Zurzeit sind keine Tiere verfügbar.";
    return;
  }
  if (tiere.length === 0) {
    bereich.textContent = "Keine Tiere gefunden. Versuch einen anderen Suchbegriff.";
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

// Bei jedem Tippen und bei jeder Auswahl neu filtern
document.getElementById("suchfeld").addEventListener("input", filtern);
document.getElementById("tierartFilter").addEventListener("change", filtern);

ladeTiere();