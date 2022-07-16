package de.fernunihagen.mci.group2.coopalgoart.shopf.test.evolution_not_used;

import processing.core.PApplet;

public class Circles {
    ColourModel model;
    Population population;
    Evolution evolution;
    int red;
    int blue;
    int yellow;
    Colours zufall;
    DNA dna;
    int farbwahl;  //Farbe des Kreises
    int counter = 0;
    float similar;
    float fitness;

    
    
    Circles (ColourModel model, Population population, Evolution evolution) {
      this.model = model;
      this.population = population;
      this.evolution = evolution;
      dna = new DNA(evolution);
      fitness = 0.0f;
      farbwahl = 0;
    }
    
      Circles (ColourModel model, Population population, Evolution evolution, DNA dna) {
      this.model = model;
      this.population = population;
      this.evolution = evolution;
      this.dna = dna;
      fitness = 0.0f;
      farbwahl = 0;
    }
    
    void run (){
      if(counter == 32){
        counter = 0;
      }
      farbwahl = (int)evolution.map(dna.genes[counter], 0.0f, 1.0f, 0.0f, 8.0f);
      counter++;
      zufall = model.getColours(farbwahl);
      red = zufall.r;
      blue = zufall.b;
      yellow = zufall.g;
      evolution.fill (red, yellow, blue);
    }
    
   
      //Fitnessfunktion - hier sollen die Farbwerte der Kreise angeglichen werden
      void calcFitnessNeighbour (int num) {
        if(num < 31){
          similar = population.kreise[num].farbwahl - population.kreise[num+1].farbwahl;
        } else {
            similar = population.kreise[num].farbwahl - population.kreise[num-3].farbwahl;
        }
        similar = Math.abs(similar);
        fitness = 1-similar/8;
      }
      
        //Fitnessfunktion - hier sollen die Farbwerte der Kreise angeglichen werden
        void calcFitnessDiagonal (int num) {
          if(num < 8){
            similar = population.kreise[num].farbwahl - population.kreise[num+16].farbwahl;
            similar = Math.abs(similar);
            fitness = 1-similar/8;
            population.kreise[num+16].fitness = 1-(similar)/8;
          }
          if (num > 7 && num < 16){
            similar = population.kreise[num].farbwahl - population.kreise[num+16].farbwahl;
            similar = Math.abs(similar);
            fitness = 1-similar/8;
            population.kreise[num+16].fitness = 1-(similar)/8;
          }
        }
    
}
