package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;
import java.awt.event.MouseEvent;

public class StartScreen extends InteractiveGraphicalObject {

    private Scene scene;

    public StartScreen(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void draw(DrawTool drawTool) {

        // Startscreen nur anzeigen, wenn scene == 1 ist
        if (scene.getScene() != 1) {
            return;
        }

        // Titel
        drawTool.setCurrentColor(Color.WHITE);
        drawTool.formatText("Arial", Font.BOLD, 50);
        drawTool.drawText(800, 250, "GRAPH RUNNER");

        // Start Button
        drawTool.drawRectangle(800, 350, 400, 80);
        drawTool.formatText("Arial", Font.BOLD, 30);
        drawTool.drawText(950, 400, "START");

        // Leaderboard Button
        drawTool.drawRectangle(800, 500, 400, 80);
        drawTool.formatText("Arial", Font.BOLD, 30);
        drawTool.drawText(870, 550, "LEADERBOARD");
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // Buttons nur anklickbar, wenn man im Startscreen ist
        if (scene.getScene() != 1) {
            return;
        }

        int mx = e.getX();
        int my = e.getY();

        // Start Button
        if (mx >= 800 && mx <= 1200 &&
                my >= 350 && my <= 430) {

            scene.setScene(2);
        }

        // Leaderboard Button
        if (mx >= 800 && mx <= 1200 &&
                my >= 500 && my <= 580) {

            scene.setScene(3);
        }
    }
}