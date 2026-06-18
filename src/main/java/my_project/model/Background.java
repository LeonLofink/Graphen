package my_project.model;

import KAGO_framework.model.GraphicalObject;
import KAGO_framework.view.DrawTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Die Background-Klasse stellt die Umsetzung eines Objekts für den Hintergrund des Apple-Games dar.
 * Hier werden sogenannte Arrays (zu deutsch: Felder) verwendet, die im Unterricht noch nicht behandelt wurden.
 */

public class Background extends GraphicalObject {
    private int background;
    private BufferedImage image;


    public Background(int background){
        this.background = background;
        try {
            image = ImageIO.read(new File(
                    "C:\\Users\\leonl\\IdeaProjects\\Graphen\\src\\main\\resources\\graphic\\Purple_Nebula_07-1024x1024.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(DrawTool drawTool) {
        if (background == 1){
        drawTool.setCurrentColor(new Color(0, 0, 0));
        drawTool.drawFilledRectangle(0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        }
        if (background == 2){
            drawTool.drawTransformedImage(image, 1000, 100,0,4);
        }
    }

    @Override
    public void update(double dt) {

    }
    public void setBackground(int background){
        this.background = background;
    }
}