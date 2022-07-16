package de.fernunihagen.mci.group2.coopalgoart.shopf.test.evolution_not_used;

import processing.core.PApplet;

public class Evolution extends PApplet {
    
    int generation = 10;
    Population popu;
      

    public void settings () {
      size(500, 500);
    }
      
    public void setup() {
      popu = new Population(this);
      frameRate (8);
      //noLoop();
    }

    public void draw () {
      popu.show();
      popu.evaluateDiagonal();
      popu.selection();
      
      
      if (frameCount == generation)                      // Anzahl der Generationen
       noLoop();
    }

    public static void main(String[] args) {
        String[] processingArgs = {"MySketch"};
        Evolution mySketch = new Evolution ();
        PApplet.runSketch(processingArgs, mySketch);

    }

}
