package my_project.model;

import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;

import java.awt.*;

public class Player extends InteractiveGraphicalObject {

    private double x;
    private double y;
    private double stamina;
    private double maxHp;
    private double luck;
    private int hp;
    private int targetNodeIndex;
    private Road road;
    private double targetX;
    private double targetY;
    private boolean moving = false;
    private boolean runSaved = false;

    public Player(double x, double y, double stamina, double maxHp, double luck) {
        this.x = x;
        this.y = y;
        this.stamina = stamina;
        this.luck = luck;
        this.maxHp = maxHp;
        hp = 200;
    }

    @Override
    public void draw(DrawTool drawTool) {
        double barWidth = 250 + getMaxHp() - 100;
        double barHeight = 25;


        drawTool.setCurrentColor(Color.DARK_GRAY);
        drawTool.drawFilledRectangle(50, 30, barWidth, barHeight);
        drawTool.setCurrentColor(Color.RED);
        drawTool.drawFilledRectangle(50, 30, (double) hp / maxHp * barWidth, barHeight);
        drawTool.setCurrentColor(Color.WHITE);
        drawTool.drawRectangle(50, 30, barWidth, barHeight);
        drawTool.drawText(20 + barWidth/2 , 30 + 18, (int) hp + " / " + (int) maxHp);
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

    public double getLuck() {
        return luck;
    }

    public double getHp() {
        return hp;
    }

    public double getMaxHp() {
        return maxHp;
    }
    public void setRoad(Road road) {
        this.road = road;
    }
    public void setTarget(double x, double y, int nodeIndex){
        targetX = x;
        targetY = y;
        targetNodeIndex = nodeIndex;
        moving = true;
    }
    @Override
    public void update(double dt){

        if(!moving) return;

        double dx = targetX - x;
        double dy = targetY - y;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist < 5){

            x = targetX;
            y = targetY;

            moving = false;

            int schaden = (int) road.getGewichtZumNachbarn(targetNodeIndex);

            if (schaden > 0) {
                hp -= schaden;
            }

            if (hp < 0) {
                hp = 0;
            }

            road.geheZuKnoten(targetNodeIndex);

            if (road.istSpielGewonnen() && !runSaved) {
                Leaderboard.addRun(hp);
                runSaved = true;
            }

            x = Toolkit.getDefaultToolkit().getScreenSize().width / 2.0;
            y = Toolkit.getDefaultToolkit().getScreenSize().height / 2.0;
        }

        double speed = 200;

        x += dx / dist * speed * dt;
        y += dy / dist * speed * dt;
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
}
