package my_project.model;
import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;
import java.awt.event.MouseEvent;


public class StartScreen extends InteractiveGraphicalObject {
    private Scene scene;

    public StartScreen() {
    }

    @Override
    public void draw(DrawTool drawTool) {

        // Titel
        drawTool.setCurrentColor(Color.WHITE);
        drawTool.formatText("Arial", Font.BOLD, 50);
        drawTool.drawText(825, 250, "NAME");

        // Start Button
        drawTool.drawRectangle(700, 350, 400, 80);
        drawTool.formatText("Arial", Font.BOLD, 30);
        drawTool.drawText(850, 400, "START");

        // BST Button
        drawTool.drawRectangle(700, 500, 400, 80);
        drawTool.formatText("Arial", Font.BOLD, 30);
        drawTool.drawText(730, 550, "BINARY SEARCH TREE");


    }
    @Override
    public void mousePressed(MouseEvent e){

        int mx = e.getX();
        int my = e.getY();

        // Start Button
        if (mx >= 350 && mx <= 650 &&
                my >= 250 && my <= 330) {

            scene.setScene(2);
        }

        // BST Button
        if (mx >= 350 && mx <= 650 &&
                my >= 380 && my <= 460) {

            scene.setScene(3);
        }
    }

}