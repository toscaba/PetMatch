// Welche Aktionen in welchem Status erlaubt sind
const AKTIONEN = {
  ZU_VERMITTELN: ["reservieren", "loeschen"],
  RESERVIERT: ["freigeben", "adoptieren", "loeschen"],
  VERMITTELT: ["loeschen"],
};

const BESCHRIFTUNG = {
  reservieren: "Reservieren",
  freigeben: "Freigeben",
  adoptieren: "Adoptieren",
  loeschen: "Löschen",
};

async function ladeTiere() {
  const antwort = await fetch("/api/tiere");
  const tiere = await antwort.json();
  const liste = document.getElementById("tierListe");
  liste.innerHTML = "";

  for (const tier of tiere) {
    if (tier.status === "GELOESCHT") continue;

    const zeile = document.createElement("tr");
    for (const wert of [tier.name, tier.tierart, tier.rasse,
                        tier.alter, tier.charakter, tier.status]) {
      const zelle = document.createElement("td");
      zelle.textContent = wert ?? "";
      zeile.appendChild(zelle);
    }

    const aktionen = document.createElement("td");
    for (const aktion of AKTIONEN[tier.status]) {
      const button = document.createElement("button");
      button.textContent = BESCHRIFTUNG[aktion];
      button.onclick = () => ausfuehren(tier.id, aktion);
      aktionen.appendChild(button);
    }
    zeile.appendChild(aktionen);
    liste.appendChild(zeile);
  }
}

async function ausfuehren(id, aktion) {
  const antwort = await fetch(`/api/tiere/${id}/${aktion}`, { method: "POST" });
  if (!antwort.ok) alert(await antwort.text());
  ladeTiere();
}

document.getElementById("tierFormular").onsubmit = async (event) => {
  event.preventDefault();
  const formular = event.target;
  const daten = Object.fromEntries(new FormData(formular));
  daten.alter = Number(daten.alter);

  const antwort = await fetch("/api/tiere", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(daten),
  });
  if (!antwort.ok) {
    alert(await antwort.text());
    return;
  }
  formular.reset();
  ladeTiere();
};

ladeTiere();
