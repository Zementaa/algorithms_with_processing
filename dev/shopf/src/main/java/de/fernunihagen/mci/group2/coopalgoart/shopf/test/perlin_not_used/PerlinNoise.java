package de.fernunihagen.mci.group2.coopalgoart.shopf.test.perlin_not_used;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class PerlinNoise extends PApplet {
    
    // Was man parametermäßig ändern kann:
    //vector.setMag Größe des Vektors (Zeile 37)
    //in Hairline maxSpeed
    //zoffset dritte Dimension, kann man damit langsamer machen
    //Zufälligkeit des Strömungsfeldes erhöhen (Zeile 36) mit TWO_PI * 4 etc.

    int scale = 10;
    int cols;
    int rows;
    float noiseValue= 0.1f;
    float zoffset = 0;
    ArrayList<Hairline> lines;
    int numLine = 800;
    PVector [] flowField;
    
    
    public void settings () {
      size (600, 600);
    }
    
    public void setup() {
      background(255);
      lines = new ArrayList<Hairline>();
          for(int i = 0; i < numLine; i++){
            lines.add(new Hairline(this));
          }
      cols= floor(width/scale);          //floor wandelt Gleitzahl in kleinere oder gleiche int Zahl um;
      rows = floor(height/scale);
      flowField = new PVector [cols * rows];  //hier wird das Strömungsfeld gespeichert
      
    }
    
    
    public void draw () {
       
      float yoffset = 0;
      for (int y = 0; y < rows; y++) {
          float xoffset = 0;
          for (int x = 0; x < cols; x++) {
            float angle = map(noise(xoffset, yoffset, zoffset), 0, 1, 0, TWO_PI * 4);  //map wandelt überträgt eine Zahl (hier zwischen 0 und 1) in eine Zahl zwischen (hier 0 und 2PI) um)
            xoffset = xoffset + noiseValue;
            PVector vector = PVector.fromAngle(angle);
            vector.setMag(1);
            int index = x + y * cols;                                              //index, wie die Vektoren im Grid im Array gespeichert werden (von links nach rechts, von oben nach unten)
            flowField[index] = vector;
            /*stroke(0, 50);   //Ansicht des Strömungsfeldes
            strokeWeight(1);
            push();
            translate (x * scale, y * scale);
            rotate(vector.heading());      //rotiert, wohin die Spitze des Vektors zeigt
            line (0, 0, scale, 0);
            pop();*/
        }
        yoffset = yoffset + noiseValue;
        zoffset = zoffset + 0.0003f;
      }
      for(int i = 0; i < lines.size(); i++){
            lines.get(i).searchVector(flowField);
            lines.get(i).move();
            lines.get(i).shapes();
            lines.get(i).edges();
          }

    }

    public static void main(String[] args) {
        String[] processingArgs = {"MySketch"};
        PerlinNoise mySketch = new PerlinNoise ();
        PApplet.runSketch(processingArgs, mySketch);

    }

}
