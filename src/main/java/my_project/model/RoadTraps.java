package my_project.model;
import java.awt.Color;
import java.util.Random;


public class RoadTraps {
    private Road road;
    Random random;
    int AnzahlTraps = 4;
    int SelectedKnoten[];

    RoadTraps() {

        private int SelectTraps(){
            for (int i = 0; i < AnzahlTraps; i++) {
                SelectedKnoten[i] = random.nextInt(road.getAnzahlKnoten()+1);
                if (road.istSpielGewonnen() == true){
                    //Playerhp = 100000;;
                }

            }



        }
    }
}