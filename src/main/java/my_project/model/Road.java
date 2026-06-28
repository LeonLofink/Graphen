package my_project.model;

import KAGO_framework.model.abitur.datenstrukturen.Graph;
import KAGO_framework.model.abitur.datenstrukturen.Vertex;
import KAGO_framework.model.abitur.datenstrukturen.Edge;

import java.util.Random;

public class Road extends Graph {

    private static  int ANZAHL_KNOTEN = 20;

    private static  int MAX_KANTEN_PRO_KNOTEN = 3;

    private Vertex[] knoten;

    private int[] grad;

    private boolean[][] verbunden;

    private double[][] gewicht;

    private Vertex start;

    private Vertex ziel;

    private Vertex aktuellerKnoten;

    private long startZeit;

    private boolean spielGewonnen;

    private Random random;

    public Road() {
        super();

        random = new Random();

        knoten = new Vertex[ANZAHL_KNOTEN];
        grad = new int[ANZAHL_KNOTEN];
        verbunden = new boolean[ANZAHL_KNOTEN][ANZAHL_KNOTEN];
        gewicht = new double[ANZAHL_KNOTEN][ANZAHL_KNOTEN];

        erstelleKnoten();
        erstelleZusammenhaengendenGraphen();
        erstelleZufaelligeZusatzKanten();

        start = knoten[0];

        ziel = findeEntferntestenKnotenVomStart(0);

        aktuellerKnoten = start;

        startZeit = System.currentTimeMillis();

        spielGewonnen = false;
    }

    private void erstelleKnoten() {
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            knoten[i] = new Vertex("Knoten " + i);
            this.addVertex(knoten[i]);
        }
    }

    private void erstelleZusammenhaengendenGraphen() {
        for (int i = 1; i < ANZAHL_KNOTEN; i++) {
            int andererKnoten;

            do {
                andererKnoten = random.nextInt(i);
            } while (grad[andererKnoten] >= MAX_KANTEN_PRO_KNOTEN);

            fuegeKanteHinzu(i, andererKnoten);
        }
    }

    private void erstelleZufaelligeZusatzKanten() {
        int anzahlZusatzKanten = random.nextInt(12);

        int erstellt = 0;
        int versuche = 0;

        while (erstellt < anzahlZusatzKanten && versuche < 300) {
            int a = random.nextInt(ANZAHL_KNOTEN);
            int b = random.nextInt(ANZAHL_KNOTEN);

            if (darfKanteErstelltWerden(a, b)) {
                fuegeKanteHinzu(a, b);
                erstellt++;
            }

            versuche++;
        }
    }

    private boolean darfKanteErstelltWerden(int a, int b) {
        if (a == b) {
            return false;
        }
        if (verbunden[a][b]) {
            return false;
        }
        if (grad[a] >= MAX_KANTEN_PRO_KNOTEN) {
            return false;
        }
        if (grad[b] >= MAX_KANTEN_PRO_KNOTEN) {
            return false;
        }
        return true;
    }

    private void fuegeKanteHinzu(int a, int b) {
        int zufallsGewicht = random.nextInt(10) + 1;
        Edge kante = new Edge(knoten[a], knoten[b], zufallsGewicht);

        this.addEdge(kante);

        verbunden[a][b] = true;
        verbunden[b][a] = true;

        gewicht[a][b] = zufallsGewicht;
        gewicht[b][a] = zufallsGewicht;

        grad[a]++;
        grad[b]++;
    }

    private Vertex findeEntferntestenKnotenVomStart(int startIndex) {
        double[] distanz = new double[ANZAHL_KNOTEN];

        boolean[] besucht = new boolean[ANZAHL_KNOTEN];

        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            distanz[i] = Double.MAX_VALUE;
        }

        distanz[startIndex] = 0;

        for (int i = 0; i < ANZAHL_KNOTEN; i++) {

            int aktuellerIndex = findeKnotenMitKleinsterDistanz(distanz, besucht);

            if (aktuellerIndex == -1) {
                break;
            }

            besucht[aktuellerIndex] = true;

            for (int nachbar = 0; nachbar < ANZAHL_KNOTEN; nachbar++) {

                if (verbunden[aktuellerIndex][nachbar]) {

                    double neueDistanz = distanz[aktuellerIndex] + gewicht[aktuellerIndex][nachbar];

                    if (neueDistanz < distanz[nachbar]) {
                        distanz[nachbar] = neueDistanz;
                    }
                }
            }
        }

        int entferntesterKnoten = startIndex;

        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (distanz[i] > distanz[entferntesterKnoten]) {
                entferntesterKnoten = i;
            }
        }

        return knoten[entferntesterKnoten];
    }

    private int findeKnotenMitKleinsterDistanz(double[] distanz, boolean[] besucht) {
        double kleinsteDistanz = Double.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (!besucht[i] && distanz[i] < kleinsteDistanz) {
                kleinsteDistanz = distanz[i];
                index = i;
            }
        }

        return index;
    }

    public boolean geheZuKnoten(int zielIndex) {
        if (spielGewonnen) {
            return false;
        }

        int aktuellerIndex = getIndexVonKnoten(aktuellerKnoten);

        if (zielIndex < 0 || zielIndex >= ANZAHL_KNOTEN) {
            return false;
        }

        if (!verbunden[aktuellerIndex][zielIndex]) {
            return false;
        }

        aktuellerKnoten = knoten[zielIndex];

        if (aktuellerKnoten == ziel) {
            spielGewonnen = true;
        }

        return true;
    }

    private int getIndexVonKnoten(Vertex vertex) {
        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (knoten[i] == vertex) {
                return i;
            }
        }
        return -1;
    }

    public int[] getNachbarnVomAktuellenKnoten() {
        int aktuellerIndex = getIndexVonKnoten(aktuellerKnoten);
        int anzahlNachbarn = grad[aktuellerIndex];

        int[] nachbarn = new int[anzahlNachbarn];

        int position = 0;

        for (int i = 0; i < ANZAHL_KNOTEN; i++) {
            if (verbunden[aktuellerIndex][i]) {
                nachbarn[position] = i;
                position++;
            }
        }

        return nachbarn;
    }

    public double getGewichtZumNachbarn(int nachbarIndex) {
        int aktuellerIndex = getIndexVonKnoten(aktuellerKnoten);

        if (nachbarIndex < 0 || nachbarIndex >= ANZAHL_KNOTEN) {
            return -1;
        }

        if (!verbunden[aktuellerIndex][nachbarIndex]) {
            return -1;
        }

        return gewicht[aktuellerIndex][nachbarIndex];
    }

    public double getVergangeneZeitInSekunden() {
        long aktuelleZeit = System.currentTimeMillis();

        return (aktuelleZeit - startZeit) / 1000.0;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getZiel() {
        return ziel;
    }

    public Vertex getAktuellerKnoten() {
        return aktuellerKnoten;
    }

    public boolean istSpielGewonnen() {
        return spielGewonnen;
    }

    public int getAnzahlKnoten() {
        return ANZAHL_KNOTEN;
    }

    public int getZielIndex() {
        return getIndexVonKnoten(ziel);
    }

}