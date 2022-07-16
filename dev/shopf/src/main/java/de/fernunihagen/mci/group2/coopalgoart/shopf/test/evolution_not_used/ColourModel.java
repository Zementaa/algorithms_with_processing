package de.fernunihagen.mci.group2.coopalgoart.shopf.test.evolution_not_used;

public class ColourModel {

    Colours[]colours = new Colours [8];

    ColourModel () {
      colours[0] = new Colours (0,0,0);  
      colours[1] = new Colours (0,0,255); 
      colours[2] = new Colours (50,80,255);
      colours[3] = new Colours (90,120,255);
      colours[4] = new Colours(130,160,255);
      colours[5] = new Colours (170,200,255);
      colours[6] = new Colours (210,240,255);
      colours[7] = new Colours (255,255,255);
    }
    
    Colours getColours (int num) {
      return colours[num];
    }
}
