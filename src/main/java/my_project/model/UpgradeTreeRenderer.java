package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.model.abitur.datenstrukturen.BinarySearchTree;
import KAGO_framework.view.DrawTool;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class UpgradeTreeRenderer extends InteractiveGraphicalObject {

    private BinarySearchTree<Upgrade> tree;
    //private Player player;
    private Upgrade selectedUpgrade;
    private Random random;

    public UpgradeTreeRenderer() {
        //this.player = player;
        this.tree = new BinarySearchTree<>();
        this.random = new Random();
        generateRandomUpgradeTree();
    }

    /**
     * Generates a balanced BST dynamically, forcing -1.0 to the left leaf and 1.0 to the right leaf.
     */
    public void generateRandomUpgradeTree() {
        tree = new BinarySearchTree<>();

        // Root element should be balanced near 0.0
        tree.insert(new Upgrade(0.0));

        // Insert inner random nodes
        tree.insert(new Upgrade(-0.5 + random.nextDouble() * 0.3)); // Left sub-branch inner node
        tree.insert(new Upgrade(0.2 + random.nextDouble() * 0.3));  // Right sub-branch inner node

        // Explicitly force the extreme leaves to fulfill assignment conditions
        tree.insert(new Upgrade(-1.0)); // Leftmost leaf
        tree.insert(new Upgrade(1.0));  // Rightmost leaf
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.setCurrentColor(Color.WHITE);
        drawTool.formatText("Arial", Font.BOLD, 30);
        drawTool.drawText(50, 80, "UPGRADE MATRIX SHOP");
        drawTool.formatText("Arial", Font.PLAIN, 18);
        drawTool.drawText(50, 110, "Left side: Health Upgrades | Right side: Speed Upgrades");

        // Recursively draw tree connections and nodes starting at screen center
        double startX = Toolkit.getDefaultToolkit().getScreenSize().width / 2.0;
        double startY = 200;
        drawTreeNodes(drawTool, tree, startX, startY, 250, 100);

        // Draw HUD details for selected upgrade
        if (selectedUpgrade != null) {
            drawTool.setCurrentColor(Color.DARK_GRAY);
            drawTool.drawFilledRectangle(50, 650, 400, 150);
            drawTool.setCurrentColor(Color.WHITE);
            drawTool.drawRectangle(50, 650, 400, 150);

            drawTool.formatText("Arial", Font.BOLD, 20);
            drawTool.drawText(70, 690, selectedUpgrade.getName());
            drawTool.formatText("Arial", Font.PLAIN, 16);
            drawTool.drawText(70, 720, "Value Orientation: " + String.format("%.2f", selectedUpgrade.getValue()));
            drawTool.drawText(70, 740, "Cost: " + selectedUpgrade.getCost() + " Stamina Tokens");

            if (selectedUpgrade.isPurchased()) {
                drawTool.setCurrentColor(Color.GREEN);
                drawTool.drawText(70, 770, "ALREADY OWNED");
            } else {
                drawTool.setCurrentColor(Color.YELLOW);
                drawTool.drawFilledRectangle(280, 735, 140, 40);
                drawTool.setCurrentColor(Color.BLACK);
                drawTool.drawText(310, 760, "CLICK TO BUY");
            }
        }
    }

    private void drawTreeNodes(DrawTool drawTool, BinarySearchTree<Upgrade> currentTree, double x, double y, double xOffset, double yOffset) {
        if (currentTree == null || currentTree.isEmpty()) return;

        Upgrade currentUpgrade = currentTree.getContent();

        // Draw path lines to children first (so circles draw on top)
        if (!currentTree.getLeftTree().isEmpty()) {
            drawTool.setCurrentColor(Color.GRAY);
            drawTool.drawLine(x, y, x - xOffset, y + yOffset);
            drawTreeNodes(drawTool, currentTree.getLeftTree(), x - xOffset, y + yOffset, xOffset * 0.5, yOffset);
        }
        if (!currentTree.getRightTree().isEmpty()) {
            drawTool.setCurrentColor(Color.GRAY);
            drawTool.drawLine(x, y, x + xOffset, y + yOffset);
            drawTreeNodes(drawTool, currentTree.getRightTree(), x + xOffset, y + yOffset, xOffset * 0.5, yOffset);
        }

        // Color coding scheme requested by prompt
        if (currentUpgrade.isPurchased()) {
            drawTool.setCurrentColor(Color.DARK_GRAY);
        } else if (currentUpgrade.getValue() == -1.0) {
            drawTool.setCurrentColor(Color.RED); // Pure Health Leaf
        } else if (currentUpgrade.getValue() == 1.0) {
            drawTool.setCurrentColor(Color.BLUE); // Pure Speed Leaf
        } else {
            drawTool.setCurrentColor(Color.CYAN); // Hybrids
        }

        if (currentUpgrade == selectedUpgrade) {
            drawTool.setCurrentColor(Color.YELLOW); // Selection highlight border
            drawTool.drawCircle(x, y, 30);
        }

        drawTool.drawFilledCircle(x, y, 25);
        drawTool.setCurrentColor(Color.BLACK);
        drawTool.formatText("Arial", Font.BOLD, 12);
        drawTool.drawText(x - 12, y + 5, String.format("%.1f", currentUpgrade.getValue()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double mx = e.getX();
        double my = e.getY();

        double startX = Toolkit.getDefaultToolkit().getScreenSize().width / 2.0;
        double startY = 200;

        // Check if user clicked a node
        Upgrade clicked = checkNodeClick(tree, mx, my, startX, startY, 250, 100);
        if (clicked != null) {
            selectedUpgrade = clicked;
        }

        // Check if buy box was clicked
        if (selectedUpgrade != null && !selectedUpgrade.isPurchased()) {
            if (mx >= 280 && mx <= 420 && my >= 735 && my <= 775) {
                // Deduct Stamina/Luck or currency from player
                selectedUpgrade.buy();
                applyUpgradeBonus(selectedUpgrade);
            }
        }
    }

    private Upgrade checkNodeClick(BinarySearchTree<Upgrade> currentTree, double mx, double my, double x, double y, double xOffset, double yOffset) {
        if (currentTree == null || currentTree.isEmpty()) return null;

        double dx = mx - x;
        double dy = my - y;
        if (Math.sqrt(dx*dx + dy*dy) < 25) {
            return currentTree.getContent();
        }

        Upgrade leftCheck = checkNodeClick(currentTree.getLeftTree(), mx, my, x - xOffset, y + yOffset, xOffset * 0.5, yOffset);
        if (leftCheck != null) return leftCheck;

        return checkNodeClick(currentTree.getRightTree(), mx, my, x + xOffset, y + yOffset, xOffset * 0.5, yOffset);
    }

    private void applyUpgradeBonus(Upgrade up) {
        // Implement status improvements directly on the player object instance
        System.out.println("Purchased upgrade: " + up.getName());
    }
}