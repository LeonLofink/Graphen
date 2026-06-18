package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;

public class Player extends InteractiveGraphicalObject {

    private double x;
    private double y;
    private double stamina;
    private double health;
    private double luck;

    public Player(double x, double y, double stamina, double health, double luck) {
        this.x = x;
        this.y = y;
        this.stamina = stamina;
        this.health = health;
        this.luck = luck;
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.setCurrentColor(new Color(0, 111, 255));
        drawTool.drawFilledCircle(x, y, 30);
        drawTool.setCurrentColor(Color.WHITE);
        drawTool.drawCircle(x, y, 30);

    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getStamina() {
        return stamina;
    }
    public double getHealth() {
        return health;
    }
    public double getLuck() {
        return luck;
    }
}
