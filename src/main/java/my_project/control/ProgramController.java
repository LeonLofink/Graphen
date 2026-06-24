package my_project.control;

import KAGO_framework.control.ViewController;
import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.model.abitur.datenstrukturen.Vertex;
import my_project.model.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ProgramController extends InteractiveGraphicalObject {

    // Referenz auf den ViewController
    private final ViewController viewController;

    // Array für 100 verschiedene Graphen
    private Road[] roads;
    private RoadRenderer roadRenderer;

    private UpgradeTreeRenderer upgradeTreeRenderer;

    private Background background;
    private StartScreen startScreen;
    private Scene scene;
    private Player player;

    private int currentScene = 1;

    public ProgramController(ViewController viewController){
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

        // Speicherplatz für 100 Road-Objekte
        roads = new Road[100];

        // 100 verschiedene zufällige Graphen erstellen
        for (int i = 0; i < roads.length; i++) {
            roads[i] = new Road();
        }

        // Beispiel: ersten Graphen ausgeben
        System.out.println("100 Graphen wurden erstellt.");
        System.out.println("Start vom ersten Graphen: " + roads[0].getStart());
        System.out.println("Ziel vom ersten Graphen: " + roads[0].getZiel());
    }

    public void updateProgram(double dt){

        if(scene.getScene() != currentScene){
            currentScene = scene.getScene();

            if(currentScene == 2){
                viewController.removeDrawable(startScreen);
                background.setBackground(2);
                player = new Player(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2,100,100,100);
                viewController.register(player);
                roads = new Road[100];

                for(int i = 0; i < roads.length; i++){
                    roads[i] = new Road();
                }

                Road currentRoad = roads[0];

                roadRenderer = new RoadRenderer(currentRoad,player);
                viewController.draw(roadRenderer);
                viewController.draw(player);
                player.setRoad(currentRoad);
            }

            if(currentScene == 3){
                viewController.removeDrawable(startScreen);
                background.setBackground(1); // Keeps background dark or custom

                if (upgradeTreeRenderer == null) {
                    // Passes their working player object into your new renderer
                    upgradeTreeRenderer = new UpgradeTreeRenderer(player);
                    viewController.draw(upgradeTreeRenderer);
                    viewController.register(upgradeTreeRenderer);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        double mx = e.getX();
        double my = e.getY();

        if (roadRenderer == null) return;

        for (int i = 0; i < roadRenderer.getNeighborCount(); i++) {

            double dx = roadRenderer.getNeighborX(i) - mx;
            double dy = roadRenderer.getNeighborY(i) - my;

            double dist = Math.sqrt(dx*dx + dy*dy);

            if (dist < 20) {
                int nodeIndex = roadRenderer.getNeighborIndex(i);
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