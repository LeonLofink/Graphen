package my_project.model;

import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;

public class RoadRenderer extends GraphicalObject {

    private Road road;
    private Player player;

    public RoadRenderer(Road road, Player player){
        this.player = player;
        this.road = road;
    }

    @Override
    public void draw(DrawTool drawTool) {

        double centerX = Toolkit.getDefaultToolkit().getScreenSize().width / 2.0;
        double centerY = Toolkit.getDefaultToolkit().getScreenSize().height / 2.0;

        int[] neighbors = road.getNachbarnVomAktuellenKnoten();

        double radius = 200;

        // Linien zeichnen
        drawTool.setCurrentColor(Color.GRAY);

        for (int i = 0; i < neighbors.length; i++) {

            double winkel = 2 * Math.PI * i / neighbors.length;

            double x = centerX + radius * Math.cos(winkel);
            double y = centerY + radius * Math.sin(winkel);

            drawTool.drawLine(centerX, centerY, x, y);
        }

        // Nachbarn zeichnen
        drawTool.setCurrentColor(Color.WHITE);

        for (int i = 0; i < neighbors.length; i++) {

            double winkel = 2 * Math.PI * i / neighbors.length;

            double x = centerX + radius * Math.cos(winkel);
            double y = centerY + radius * Math.sin(winkel);

            drawTool.drawFilledCircle(x, y, 15);
        }

        // Spieler in der Mitte
        drawTool.setCurrentColor(Color.RED);
        drawTool.drawFilledCircle(player.getX(), player.getY(), 20);
    }

    @Override
    public void update(double dt) {
        // optional später Animation / Layout
    }
}
