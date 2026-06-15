package my_project.control;

import KAGO_framework.control.ViewController;
import KAGO_framework.model.InteractiveGraphicalObject;
import my_project.model.*;

public class ProgramController extends InteractiveGraphicalObject {

    // Referenz auf den ViewController
    private final ViewController viewController;

    // Array für 100 verschiedene Graphen
    private Road[] roads;

    private Background background;
    private StartScreen startScreen;
    private Scene scene;

    public ProgramController(ViewController viewController){
        this.viewController = viewController;
    }

    public void startProgram() {

        scene = new Scene();
        viewController.draw(scene);


        background = new Background();
        viewController.draw(background);

        if (scene.getScene() == 1){
            startScreen = new StartScreen();
            viewController.draw(startScreen);
            viewController.register(startScreen);
        }
        if (scene.getScene() == 2){
            viewController.removeDrawable(startScreen);
        }
        if (scene.getScene() == 3){
            viewController.removeDrawable(startScreen);
        }
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

    }
}