package my_project.control;

import KAGO_framework.control.ViewController;
import KAGO_framework.model.InteractiveGraphicalObject;
import my_project.model.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ProgramController extends InteractiveGraphicalObject {

    // Referenz auf den ViewController
    private final ViewController viewController;

    // Array für 100 verschiedene Graphen
    private Road[] roads;

    // Der aktuelle Graph, der gerade gespielt wird
    private Road currentRoad;

    private RoadRenderer roadRenderer;
    private Background background;
    private StartScreen startScreen;
    private Scene scene;
    private Player player;
    private Leaderboard leaderboard;

    private int currentScene = 1;

    public ProgramController(ViewController viewController) {
        this.viewController = viewController;
    }

    public void startProgram() {
        viewController.register(this);

        scene = new Scene();
        viewController.draw(scene);

        background = new Background(1);
        viewController.draw(background);

        startScreen = new StartScreen(scene);
        viewController.draw(startScreen);
        viewController.register(startScreen);

        leaderboard = new Leaderboard(scene);
        viewController.draw(leaderboard);
        viewController.register(leaderboard);

        // Speicherplatz für 100 Road-Objekte
        roads = new Road[100];

        // 100 verschiedene zufällige Graphen erstellen
        for (int i = 0; i < roads.length; i++) {
            roads[i] = new Road();
        }

        System.out.println("100 Graphen wurden erstellt.");
        System.out.println("Start vom ersten Graphen: " + roads[0].getStart());
        System.out.println("Ziel vom ersten Graphen: " + roads[0].getZiel());
    }

    public void updateProgram(double dt) {

        // Wenn die Szene gewechselt wurde
        if (scene.getScene() != currentScene) {
            currentScene = scene.getScene();

            // STARTSCREEN / MENÜ
            if (currentScene == 1) {
                background.setBackground(1);

                viewController.draw(startScreen);

                if (roadRenderer != null) {
                    viewController.removeDrawable(roadRenderer);
                    roadRenderer = null;
                }

                if (player != null) {
                    viewController.removeDrawable(player);
                    player = null;
                }

                currentRoad = null;
            }

            // SPIEL
            if (currentScene == 2) {

                viewController.removeDrawable(startScreen);
                background.setBackground(2);

                player = new Player(
                        Toolkit.getDefaultToolkit().getScreenSize().width / 2.0,
                        Toolkit.getDefaultToolkit().getScreenSize().height / 2.0,
                        100,
                        100,
                        100
                );

                viewController.register(player);

                roads = new Road[100];

                for (int i = 0; i < roads.length; i++) {
                    roads[i] = new Road();
                }

                currentRoad = roads[0];

                roadRenderer = new RoadRenderer(currentRoad, player);

                viewController.draw(roadRenderer);
                viewController.draw(player);

                player.setRoad(currentRoad);
            }

            // LEADERBOARD
            if (currentScene == 3) {

                viewController.removeDrawable(startScreen);
                background.setBackground(1);

                if (roadRenderer != null) {
                    viewController.removeDrawable(roadRenderer);
                    roadRenderer = null;
                }

                if (player != null) {
                    viewController.removeDrawable(player);
                    player = null;
                }

                currentRoad = null;
            }
        }

        // Wenn man im Spiel gewinnt: zurück ins Menü
        if (scene.getScene() == 2 && currentRoad != null && currentRoad.istSpielGewonnen()) {
            scene.setScene(1);
            currentRoad = null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // Nur im Spiel darf man Knoten anklicken
        if (scene.getScene() != 2 || roadRenderer == null || player == null) {
            return;
        }

        double mx = e.getX();
        double my = e.getY();

        for (int i = 0; i < roadRenderer.getNeighborCount(); i++) {

            double dx = roadRenderer.getNeighborX(i) - mx;
            double dy = roadRenderer.getNeighborY(i) - my;

            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist < 20) {
                player.setTarget(
                        roadRenderer.getNeighborX(i),
                        roadRenderer.getNeighborY(i),
                        roadRenderer.getNeighborIndex(i)
                );
                break;
            }
        }
    }
}