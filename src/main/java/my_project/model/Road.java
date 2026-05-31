package my_project.model;

import KAGO_framework.model.abitur.datenstrukturen.Graph;
import KAGO_framework.model.abitur.datenstrukturen.Vertex;
import KAGO_framework.model.abitur.datenstrukturen.Edge;

import java.util.Random;

public class Road extends Graph {

    // Anzahl der Knoten im Graphen
    private static final int ANZAHL_KNOTEN = 20;

    // Jeder Knoten darf maximal 3 Verbindungen haben
    private static final int MAX_KANTEN_PRO_KNOTEN = 3;

    // Array für alle Knoten
    private Vertex[] knoten;

    // Speichert, wie viele Kanten jeder Knoten hat
    private int[] grad;

    // Speichert, ob zwei Knoten verbunden sind
    private boolean[][] verbunden;

    // Speichert das Gewicht zwischen zwei Knoten
    private double[][] gewicht;

    // Startknoten des Spiels
    private Vertex start;

    // Zielknoten des Spiels
    private Vertex ziel;

    // Knoten, auf dem der Spieler gerade steht
    private Vertex aktuellerKnoten;

    // Startzeit für den Speedrun
    private long startZeit;

    // Speichert, ob das Spiel gewonnen wurde
    private boolean spielGewonnen;

    // Zufallsgenerator
    private Random random;

    public Road() {
        super();

        // Zufallsgenerator erstellen
        random = new Random();

        // Speicherplätze vorbereiten
        knoten = new Vertex[ANZAHL_KNOTEN];
        grad = new int[ANZAHL_KNOTEN];
        verbunden = new boolean[ANZAHL_KNOTEN][ANZAHL_KNOTEN];
        gewicht = new double[ANZAHL_KNOTEN][ANZAHL_KNOTEN];

        // Graph aufbauen
        erstelleKnoten();
        erstelleZusammenhaengendenGraphen();
        erstelleZufaelligeZusatzKanten();

        // Start ist immer Knoten 0
        start = knoten[0];

        // Ziel ist der Knoten, der am weitesten vom Start entfernt ist
        ziel = findeEntferntestenKnotenVomStart(0);

        // Spieler startet beim Startknoten
        aktuellerKnoten = start;

        // Zeitmessung startet
        startZeit = System.currentTimeMillis();

        // Am Anfang ist das Spiel noch nicht gewonnen
        spielGewonnen = false;
    }

    private void erstelleKnoten() {
        // Erstellt 20 Knoten
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {

            // Neuer Knoten mit Namen
            knoten[i] = new Vertex("Knoten " + i);

            // Knoten zum Graphen hinzufügen
            this.addVertex(knoten[i]);
        }
    }

    private void erstelleZusammenhaengendenGraphen() {
        // Sorgt dafür, dass jeder Knoten erreichbar ist
        for (int i = 1; i < ANZAHL_KNOTEN; i++) {
            int andererKnoten;

            // Suche einen vorherigen Knoten, der noch Platz für eine Kante hat
            do {
                andererKnoten = random.nextInt(i);
            } while (grad[andererKnoten] >= MAX_KANTEN_PRO_KNOTEN);

            // Verbinde den aktuellen Knoten mit dem gefundenen Knoten
            fuegeKanteHinzu(i, andererKnoten);
        }
    }

    private void erstelleZufaelligeZusatzKanten() {
        // Zufällige Anzahl an zusätzlichen Kanten von 0 bis 11
        int anzahlZusatzKanten = random.nextInt(12);

        int erstellt = 0;
        int versuche = 0;

        // Versucht zusätzliche Kanten zu erstellen
        while (erstellt < anzahlZusatzKanten && versuche < 300) {

            // Zwei zufällige Knoten auswählen
            int a = random.nextInt(ANZAHL_KNOTEN);
            int b = random.nextInt(ANZAHL_KNOTEN);

            // Prüfen, ob die Kante erlaubt ist
            if (darfKanteErstelltWerden(a, b)) {

                // Kante hinzufügen
                fuegeKanteHinzu(a, b);
                erstellt++;
            }

            // Versuch zählen, damit keine Endlosschleife entsteht
            versuche++;
        }
    }

    private boolean darfKanteErstelltWerden(int a, int b) {
        // Ein Knoten darf nicht mit sich selbst verbunden werden
        if (a == b) {
            return false;
        }

        // Die Kante darf nicht doppelt existieren
        if (verbunden[a][b]) {
            return false;
        }

        // Knoten a darf maximal 3 Kanten haben
        if (grad[a] >= MAX_KANTEN_PRO_KNOTEN) {
            return false;
        }

        // Knoten b darf maximal 3 Kanten haben
        if (grad[b] >= MAX_KANTEN_PRO_KNOTEN) {
            return false;
        }

        // Wenn alles passt, darf die Kante erstellt werden
        return true;
    }

    private void fuegeKanteHinzu(int a, int b) {
        // Zufälliges Gewicht zwischen 1 und 10
        int zufallsGewicht = random.nextInt(10) + 1;

        // Neue Kante erstellen
        Edge kante = new Edge(knoten[a], knoten[b], zufallsGewicht);

        // Kante zum Graphen hinzufügen
        this.addEdge(kante);

        // Verbindung in beide Richtungen speichern
        verbunden[a][b] = true;
        verbunden[b][a] = true;

        // Gewicht in beide Richtungen speichern
        gewicht[a][b] = zufallsGewicht;
        gewicht[b][a] = zufallsGewicht;

        // Beide Knoten haben jetzt eine Kante mehr
        grad[a]++;
        grad[b]++;
    }

    private Vertex findeEntferntestenKnotenVomStart(int startIndex) {
        // Speichert die kürzeste Distanz vom Start zu jedem Knoten
        double[] distanz = new double[ANZAHL_KNOTEN];

        // Speichert, ob ein Knoten schon besucht wurde
        boolean[] besucht = new boolean[ANZAHL_KNOTEN];

        // Am Anfang sind alle Distanzen unendlich groß
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            distanz[i] = Double.MAX_VALUE;
        }

        // Der Startknoten hat die Distanz 0 zu sich selbst
        distanz[startIndex] = 0;

        // Dijkstra-Algorithmus
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {

            // Suche den unbesuchten Knoten mit der kleinsten Distanz
            int aktuellerIndex = findeKnotenMitKleinsterDistanz(distanz, besucht);

            // Falls kein Knoten gefunden wurde, abbrechen
            if (aktuellerIndex == -1) {
                break;
            }

            // Knoten als besucht markieren
            besucht[aktuellerIndex] = true;

            // Alle Nachbarn prüfen
            for (int nachbar = 0; nachbar < ANZAHL_KNOTEN; nachbar++) {

                // Nur wenn eine Verbindung existiert
                if (verbunden[aktuellerIndex][nachbar]) {

                    // Neue mögliche Distanz berechnen
                    double neueDistanz = distanz[aktuellerIndex] + gewicht[aktuellerIndex][nachbar];

                    // Wenn der neue Weg kürzer ist, speichern
                    if (neueDistanz < distanz[nachbar]) {
                        distanz[nachbar] = neueDistanz;
                    }
                }
            }
        }

        // Startwert für den entferntesten Knoten
        int entferntesterKnoten = startIndex;

        // Suche den Knoten mit der größten Distanz
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (distanz[i] > distanz[entferntesterKnoten]) {
                entferntesterKnoten = i;
            }
        }

        // Dieser Knoten wird das Ziel
        return knoten[entferntesterKnoten];
    }

    private int findeKnotenMitKleinsterDistanz(double[] distanz, boolean[] besucht) {
        double kleinsteDistanz = Double.MAX_VALUE;
        int index = -1;

        // Suche den unbesuchten Knoten mit der kleinsten Distanz
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (!besucht[i] && distanz[i] < kleinsteDistanz) {
                kleinsteDistanz = distanz[i];
                index = i;
            }
        }

        return index;
    }

    public boolean geheZuKnoten(int zielIndex) {
        // Wenn das Spiel schon gewonnen ist, darf man sich nicht mehr bewegen
        if (spielGewonnen) {
            return false;
        }

        // Index vom aktuellen Knoten herausfinden
        int aktuellerIndex = getIndexVonKnoten(aktuellerKnoten);

        // Ungültiger Zielindex
        if (zielIndex < 0 || zielIndex >= ANZAHL_KNOTEN) {
            return false;
        }

        // Man darf nur zu einem verbundenen Nachbarknoten gehen
        if (!verbunden[aktuellerIndex][zielIndex]) {
            return false;
        }

        // Spieler bewegt sich zum neuen Knoten
        aktuellerKnoten = knoten[zielIndex];

        // Wenn der Spieler beim Ziel angekommen ist, ist das Spiel gewonnen
        if (aktuellerKnoten == ziel) {
            spielGewonnen = true;
        }

        return true;
    }

    private int getIndexVonKnoten(Vertex vertex) {
        // Sucht den Index eines Knotens im Array
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (knoten[i] == vertex) {
                return i;
            }
        }

        // Falls der Knoten nicht gefunden wurde
        return -1;
    }

    public int[] getNachbarnVomAktuellenKnoten() {
        // Index vom aktuellen Knoten
        int aktuellerIndex = getIndexVonKnoten(aktuellerKnoten);

        // Anzahl der Nachbarn
        int anzahlNachbarn = grad[aktuellerIndex];

        // Array für alle Nachbarn
        int[] nachbarn = new int[anzahlNachbarn];

        int position = 0;

        // Alle verbundenen Knoten suchen
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (verbunden[aktuellerIndex][i]) {
                nachbarn[position] = i;
                position++;
            }
        }

        return nachbarn;
    }

    public double getGewichtZumNachbarn(int nachbarIndex) {
        // Index vom aktuellen Knoten
        int aktuellerIndex = getIndexVonKnoten(aktuellerKnoten);

        // Ungültiger Index
        if (nachbarIndex < 0 || nachbarIndex >= ANZAHL_KNOTEN) {
            return -1;
        }

        // Wenn es keine Verbindung gibt
        if (!verbunden[aktuellerIndex][nachbarIndex]) {
            return -1;
        }

        // Gewicht der Verbindung zurückgeben
        return gewicht[aktuellerIndex][nachbarIndex];
    }

    public double getVergangeneZeitInSekunden() {
        // Aktuelle Zeit holen
        long aktuelleZeit = System.currentTimeMillis();

        // Vergangene Zeit in Sekunden berechnen
        return (aktuelleZeit - startZeit) / 1000.0;
    }

    public Vertex getStart() {
        // Gibt den Startknoten zurück
        return start;
    }

    public Vertex getZiel() {
        // Gibt den Zielknoten zurück
        return ziel;
    }

    public Vertex getAktuellerKnoten() {
        // Gibt zurück, wo der Spieler gerade ist
        return aktuellerKnoten;
    }

    public boolean istSpielGewonnen() {
        // Gibt zurück, ob das Spiel gewonnen wurde
        return spielGewonnen;
    }
}