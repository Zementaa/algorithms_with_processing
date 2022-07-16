package de.fernunihagen.mci.group2.coopalgoart.shopf.test.evolution_not_used;

public class DNA {

        float[] genes;
        Evolution evolution;
        
        //32 Ringe
        int len = 32;
       
        DNA(Evolution evolution) {
          this.evolution = evolution;
          genes = new float[len];
          for (int i = 0; i < genes.length; i++) {
            
            //eine zufällige Zahl zwischen 0 und 1, die dann wiederum zufällig auf die Farben gemappt wird
            genes[i] = this.evolution.random(0,1);
          }
        }
        
        DNA(Evolution evolution, int num) {
          this.evolution = evolution;
          genes = new float [num];
        }
        
        //hier wird nur zufällig ein Elternteil als neues Kind gewählt, bei
        //anderen Verläufen, könnte man aber auch Teile des einen Elternteil mit dem anderen kreuzen etc.
        DNA crossover(DNA dna) {
          DNA child = new DNA(evolution, len);
          int middle = (int)evolution.random(genes.length);
          for (int i = 0; i < genes.length; i++) {
            if (i > middle){
              child.genes[i] = genes[i];
            } else {
              child.genes[i] = dna.genes[i];
              }
          }
          return child;
        }
}
