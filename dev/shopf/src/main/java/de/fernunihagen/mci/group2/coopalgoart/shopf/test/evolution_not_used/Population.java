package de.fernunihagen.mci.group2.coopalgoart.shopf.test.evolution_not_used;

import java.util.ArrayList;

public class Population {
    Evolution evolution;
    int circleSize = 400;
    int ringSize = evolution.floor(circleSize/8);
    ColourModel model= new ColourModel();
    DNA dna;
    Circles[] kreise;
    ArrayList <DNA> parentspool;
   
    //welche Fitnessfunktion soll gewählt werden, ENUM machen
    String function1 = "calcFitnessNeighbour";  

    
    Population(Evolution evolution) {
      this.evolution = evolution;
      kreise = new Circles [32];
      for (int i = 0; i < kreise.length; i++){
        kreise[i] = new Circles(model, this, evolution);
      }
    }
      
      //Kreise mit 32 Abschnitten werden gezeichnet
      void show (){
        evolution.noStroke();
        for (int i = 0; i < 8; i++){
          kreise[i].run();
          evolution.arc (evolution.width/2, evolution.height/2, circleSize, circleSize, 0, evolution.HALF_PI);
          kreise[i+8].run();
          evolution.arc (evolution.width/2, evolution.height/2, circleSize, circleSize, evolution.HALF_PI, evolution.PI);
          kreise[i+16].run();
          evolution.arc (evolution.width/2, evolution.height/2, circleSize, circleSize, evolution.PI, evolution.PI+evolution.HALF_PI);
          kreise[i+24].run();
          evolution.arc (evolution.width/2, evolution.height/2, circleSize, circleSize, evolution.PI+evolution.HALF_PI, 2*evolution.PI);
          circleSize = circleSize - ringSize;
        }
        circleSize = 400;
      }
    
    //Die Kreise werden proportional zu ihrer Fitness in den Parentpool geworfen
    void evaluate () {
     for(int i = 0; i< kreise.length; i++){
       kreise[i].calcFitnessNeighbour(i);
      }
      parentspool = new ArrayList<DNA>();
        for(int i = 0; i< kreise.length; i++){
          //je höher die Fitness, desto mehr Kreise dieser Farbe sind im Parentspool um die neue Generation zu finden
       float p = kreise[i].fitness*100;
         for(int j = 0; j< p; j++){
           parentspool.add(kreise[i].dna);
         }
      }
    }
    
    //Die Kind Generation wird ausgewählt, durch Kreuzung der Elternteile
    void selection () {
      for(int i = 0; i < kreise.length; i++){
        int a = evolution.floor(evolution.random(parentspool.size()));
        int b = evolution.floor(evolution.random(parentspool.size()));
        DNA parentA = parentspool.get(a);
        DNA parentB = parentspool.get(b);
        DNA child = parentA.crossover(parentB);
        kreise[i] = new Circles(model, this, evolution, child);
      }
    }
    
      void evaluateDiagonal () {
       for(int i = 0; i< kreise.length; i++){
           kreise[i].calcFitnessDiagonal(i);
        }
            parentspool = new ArrayList<DNA>();
        for(int i = 0; i< kreise.length; i++){
          //je höher die Fitness, desto mehr Kreise dieser Farbe sind im Parentspool um die neue Generation zu finden
       float p = kreise[i].fitness*100;
         for(int j = 0; j< p; j++){
           parentspool.add(kreise[i].dna);
         }
      }
      }
    
}
