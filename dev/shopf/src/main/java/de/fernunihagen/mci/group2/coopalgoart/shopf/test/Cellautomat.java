package de.fernunihagen.mci.group2.coopalgoart.shopf.test;

   import java.awt.Color;

    import processing.core.PApplet;

    public class Cellautomat extends PApplet {
        
        int [][] grid, nextGrid;
        int resolution = 10;
        int cols;
        int rows;

        
        
        @Override
        public void settings () {
            size (500, 500);
            //frameRate(2);
            cols = width/resolution;
            rows = height/resolution;
            grid = new int [cols] [rows];
            
            nextGrid = new int [cols] [rows];
            for (int i = 2; i < cols-2; i++){
              for (int j = 2; j < rows-2; j++){
              grid [i] [j] = 1;
              }
            }
            
            /*int x = cols / 2;                  //die Mitte des Grids finden, Startaufstellung 3x3 Kästchen
            int y = rows / 2;
            grid[x][y] = 1;
            grid[x-1][y] = 1;
            grid[x+1][y] = 1;
            grid[x][y-1] = 1;
            grid[x][y+1] = 1;
            grid[x-1][y-1] = 1;
            grid[x+1][y-1] = 1;
            grid[x-1][y+1] = 1;
            grid[x+1][y+1] = 1;
            
            grid[x-2][y] = 1;
            grid[x+2][y] = 1;
            grid[x+2][y+1] = 1;
            grid[x+2][y+2] = 1;
            grid[x+1][y+2] = 1;
            grid[x][y+2] = 1;
            grid[x-2][y+1] = 1;
            grid[x-2][y+2] = 1;
            grid[x-1][y+2] = 1;
            grid[x-2][y-1] = 1;
            grid[x-2][y-2] = 1;
            grid[x-1][y-2] = 1;
            grid[x][y-2] = 1;
            grid[x+1][y-2] = 1;
            grid[x+2][y-2] = 1;
            grid[x+2][y-1] = 1;*/
           
        }
        
        
        @Override
        public void draw (){
            background(0);
            frameRate(2);
            Color random = new Color ((int)random(255), (int) random(255), (int) random(255));    //jede Generation hat eigene Farbe
            for (int i = 1; i < cols-1; i++){
              for (int j = 1; j < rows-1; j++){
                int x = i * resolution;
                int y = j * resolution;
                int nb = neighbours(i, j);                //Nachbarn zählen
                if ((grid[i][j] == 1) && (nb < 2)){
                  nextGrid [i][j] = 0;
                  fill(0);                                //Rechtecke zeichnen
                  ellipse (x, y, resolution, resolution);
                } else if ((grid[i][j] == 1) && (nb > 3)){  //Regeln
                  nextGrid [i][j] = 0;
                  fill(0);
                  ellipse (x, y, resolution, resolution);
                } else if ((grid [i][j] == 0) && (nb == 3)){
                  nextGrid [i][j] = 1;
                  fill(random.getRed(), random.getGreen(), random.getBlue());
                  //fill(random(255), random(255), random(255));
                  ellipse (x, y, resolution, resolution);
                } else {
                  nextGrid[i][j] = grid [i][j];
                }
              }
            }
            int [][] temp = grid;                        //Arrays tauschen
            grid = nextGrid;
            nextGrid = temp;
               if (frameCount == 10)                      // Anzahl der Generationen
             noLoop();
          }

            int neighbours (int x, int y){
              return grid [x][y-1] +
                   grid [x+1][y-1] +
                   grid [x+1][y] +
                   grid [x+1][y+1] +
                   grid [x][y+1] +
                   grid [x-1][y+1] +
                   grid [x-1][y] +
                   grid [x-1][y-1];
            }
        
            
            /*int neighbours (int x, int y){                //Nachbarn mit starrem Rand, noch nicht richtig
            int count;
            if (x == 0 && y > 0){
              count = grid [x][y-1] +
                   grid [x+1][y-1] +
                   grid [x+1][y] +
                   grid [x+1][y+1] +
                   grid [x][y+1];
            } else if (y == 0 && x > 0){
             count = grid [x+1][y] +
                   grid [x+1][y+1] +
                   grid [x][y+1] +
                   grid [x-1][y+1] +
                   grid [x-1][y];
              } else if (x == cols-1 && y < rows-1){
                count = grid [x][y-1] +
                   grid [x][y+1] +
                   grid [x-1][y+1] +
                   grid [x-1][y] +
                   grid [x-1][y-1];
              } else if (y == rows-1 && x < cols-1){
                 count = grid [x][y-1] +
                   grid [x+1][y-1] +
                   grid [x+1][y] +
                   grid [x-1][y] +
                   grid [x-1][y-1];
              } else if (x == 0 && y == 0){
                  count = grid [x+1][y] +
                   grid [x+1][y+1] +
                   grid [x][y+1];
              } else if (x == cols-1 && y == rows-1){
                  count = grid [x][y-1] +
                   grid [x-1][y] +
                   grid [x-1][y-1];
              } else {
             count = grid [x][y-1] +
                   grid [x+1][y-1] +
                   grid [x+1][y] +
                   grid [x+1][y+1] +
                   grid [x][y+1] +
                   grid [x-1][y+1] +
                   grid [x-1][y] +
                   grid [x-1][y-1];
            }
            return count;
          }*/
        
        public static void main(String[] args) {
            String[] processingArgs = {"MySketch"};
            Cellautomat mySketch = new Cellautomat();
            PApplet.runSketch(processingArgs, mySketch);
        }



}



