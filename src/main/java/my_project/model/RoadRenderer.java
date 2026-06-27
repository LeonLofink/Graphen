package my_project.model;

import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;

public class RoadRenderer extends GraphicalObject {

    private Road road;
    private Player player;

    private double[] neighborX;
    private double[] neighborY;
    private int[] neighborIndex;

    public RoadRenderer(Road road, Player player){
        this.player = player;
        this.road = road;
    }

    @Override
    public void draw(DrawTool drawTool) {
        int[] neighbors = road.getNachbarnVomAktuellenKnoten();

        neighborX = new double[neighbors.length];
        neighborY = new double[neighbors.length];
        neighborIndex = new int[neighbors.length];

        double centerX = Toolkit.getDefaultToolkit().getScreenSize().width / 2.0;
        double centerY = Toolkit.getDefaultToolkit().getScreenSize().height / 2.0;

        double radius = 300;

        // Linien + speichern
        drawTool.setCurrentColor(Color.GRAY);

        for (int i = 0; i < neighbors.length; i++) {
            double winkel = 2 * Math.PI * i / neighbors.length;

            double x = centerX + radius * Math.cos(winkel);
            double y = centerY + radius * Math.sin(winkel);

            neighborX[i] = x;
            neighborY[i] = y;
            neighborIndex[i] = neighbors[i];

            drawTool.drawLine(centerX, centerY, x, y);
        }

        // Nachbarknoten zeichnen
        for (int i = 0; i < neighbors.length; i++) {

            if (neighborIndex[i] == road.getZielIndex()) {
                drawTool.setCurrentColor(Color.GREEN); // Zielknoten
            } else {
                drawTool.setCurrentColor(Color.WHITE); // normaler Nachbarknoten
            }

            drawTool.drawFilledCircle(neighborX[i], neighborY[i], 15);
        }

        // Nummern an die Punkte schreiben
        drawTool.setCurrentColor(Color.BLACK);

        for (int i = 0; i < neighbors.length; i++) {
            drawTool.drawText(
                    neighborX[i] - 5,
                    neighborY[i] - 5,
                    String.valueOf(neighborIndex[i])
            );
        }

        // Spieler in der Mitte
        drawTool.setCurrentColor(Color.RED);
        drawTool.drawFilledCircle(player.getX(), player.getY(), 20);
    }



    public int getNeighborCount() {
        return neighborIndex.length;
    }

    public double getNeighborX(int i) {
        return neighborX[i];
    }

    public double getNeighborY(int i) {
        return neighborY[i];
    }

    public int getNeighborIndex(int i) {
        return neighborIndex[i];
    }
}