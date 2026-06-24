package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.event.MouseEvent;

public class Scene extends InteractiveGraphicalObject {

    private int scene;
    public Scene() {
        scene = 1;
    }

    @Override
    public void draw(DrawTool drawTool) {

    }

    public int getScene() {
        return scene;
    }
    public void setScene(int scene) {
        this.scene = scene;
    }

}
