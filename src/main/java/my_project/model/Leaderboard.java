package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Leaderboard extends InteractiveGraphicalObject {

    private Scene scene;

    private static Node root;
    private static int runCount = 0;

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

    public static void addRun(int hp) {
        runCount++;
        root = insert(root, hp, runCount);
    }

    private static Node insert(Node current, int hp, int runNumber) {

        if (current == null) {
            return new Node(hp, runNumber);
        }

        if (hp < current.hp) {
            current.left = insert(current.left, hp, runNumber);
        }

        else {
            current.right = insert(current.right, hp, runNumber);
        }

        return current;
    }

    @Override
    public void draw(DrawTool drawTool) {

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

            drawTreeDescending(drawTool, root, y, place);
        }

        drawTool.setCurrentColor(Color.WHITE);
        drawTool.drawRectangle(60, 60, 250, 70);

        drawTool.formatText("Arial", Font.BOLD, 28);
        drawTool.drawText(100, 105, "ZURÜCK");
    }

    private void drawTreeDescending(DrawTool drawTool, Node current, int[] y, int[] place) {

        if (current == null) {
            return;
        }

        drawTreeDescending(drawTool, current.right, y, place);

        drawTool.drawText(740, y[0], place[0] + ".");
        drawTool.drawText(900, y[0], "Run " + current.runNumber);
        drawTool.drawText(1120, y[0], current.hp + " HP");

        y[0] += 45;
        place[0]++;

        drawTreeDescending(drawTool, current.left, y, place);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (scene.getScene() != 3) {
            return;
        }

        int mx = e.getX();
        int my = e.getY();

        if (mx >= 60 && mx <= 310 &&
                my >= 60 && my <= 130) {

            scene.setScene(1);
        }
    }
}