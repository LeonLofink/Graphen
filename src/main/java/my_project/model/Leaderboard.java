package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Leaderboard extends InteractiveGraphicalObject {

    private Scene scene;

    private static Node root;
    private static int runCount = 0;

    // Knoten für den Binary Search Tree
    private static class Node {
        int hp;
        int runNumber;

        Node left;
        Node right;

        public Node(int hp, int runNumber) {
            this.hp = hp;
            this.runNumber = runNumber;
        }
    }

    public Leaderboard(Scene scene) {
        this.scene = scene;
    }

    // Wird vom Player aufgerufen, wenn ein Run gewonnen wurde
    public static void addRun(int hp) {
        runCount++;
        root = insert(root, hp, runCount);
    }

    // Einfügen in den Binary Search Tree
    private static Node insert(Node current, int hp, int runNumber) {

        if (current == null) {
            return new Node(hp, runNumber);
        }

        // Kleinere HP nach links
        if (hp < current.hp) {
            current.left = insert(current.left, hp, runNumber);
        }

        // Größere oder gleiche HP nach rechts
        else {
            current.right = insert(current.right, hp, runNumber);
        }

        return current;
    }

    @Override
    public void draw(DrawTool drawTool) {

        // Leaderboard nur anzeigen, wenn Szene 3 aktiv ist
        if (scene.getScene() != 3) {
            return;
        }

        drawTool.setCurrentColor(Color.BLACK);
        drawTool.drawFilledRectangle(
                0,
                0,
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height
        );

        drawTool.setCurrentColor(Color.WHITE);
        drawTool.formatText("Arial", Font.BOLD, 50);
        drawTool.drawText(760, 180, "LEADERBOARD");

        drawTool.formatText("Arial", Font.BOLD, 30);
        drawTool.drawText(720, 260, "PLACE");
        drawTool.drawText(880, 260, "RUN");
        drawTool.drawText(1080, 260, "REMAINING HP");

        drawTool.drawLine(700, 280, 1300, 280);

        drawTool.formatText("Arial", Font.PLAIN, 28);

        if (root == null) {
            drawTool.drawText(760, 340, "Noch keine Runs gespeichert.");
        } else {
            int[] y = {330};
            int[] place = {1};

            // Reverse Inorder: rechts, mitte, links
            // Dadurch stehen die höchsten HP oben
            drawTreeDescending(drawTool, root, y, place);
        }

        // Zurück-Button
        drawTool.setCurrentColor(Color.WHITE);
        drawTool.drawRectangle(60, 60, 250, 70);

        drawTool.formatText("Arial", Font.BOLD, 28);
        drawTool.drawText(100, 105, "ZURÜCK");
    }

    private void drawTreeDescending(DrawTool drawTool, Node current, int[] y, int[] place) {

        if (current == null) {
            return;
        }

        // Erst rechter Teilbaum: größere HP
        drawTreeDescending(drawTool, current.right, y, place);

        drawTool.drawText(740, y[0], place[0] + ".");
        drawTool.drawText(900, y[0], "Run " + current.runNumber);
        drawTool.drawText(1120, y[0], current.hp + " HP");

        y[0] += 45;
        place[0]++;

        // Dann linker Teilbaum: kleinere HP
        drawTreeDescending(drawTool, current.left, y, place);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // Nur im Leaderboard anklickbar
        if (scene.getScene() != 3) {
            return;
        }

        int mx = e.getX();
        int my = e.getY();

        // Zurück-Button
        if (mx >= 60 && mx <= 310 &&
                my >= 60 && my <= 130) {

            scene.setScene(1);
        }
    }
}