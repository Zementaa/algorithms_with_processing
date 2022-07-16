package perlinNoise;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import processing.core.PApplet;
import processing.core.PGraphics;

public class PerlinNoise extends PApplet{
	
	//set the global variable
	int cols,rows;
	float[][] scape;
	public static int size1;
	public static int size2;
	public static int scale;
	public static int wi;
	public static int he;
	public static float flyA;
	public static double increasefly;
	public static double xoff;
	public static double yoff;
	public static int strokevalue;
	Random r = new Random();
	CooperationContext contextC;


	public PerlinNoise(CooperationContext context, PGraphics applet, int scl, int w, int h, float fly,
			double increase, double xOff, double yOff, int stroke) {
				size1 = applet.width;
				size2 = applet.height;
				scale = scl;
				wi = w;
				he = h;
				flyA = fly;
				increasefly = increase;
				xoff = xOff;
				yoff = yOff;
				strokevalue = stroke;
				
				//get seed for coorperation
				r.setSeed(context.getSeed());
				contextC = context;
				
	}
	
	public void setup(CooperationContext context, PGraphics pg) {
		
		//calc colums and rows
		cols = wi / scale;
		rows = he / scale;
		scape = new float[cols][rows];
	}
	
	public void draw(CooperationContext context, PGraphics pg) {
		
		//increase the startponit y called fly
		flyA -= increasefly;
		float Yoff = flyA;
		float Xoff = 0;

		
		
		pg.stroke(strokevalue);
		pg.noFill();
		
		//rotate the pixels on the screen
		
		int strokeColor = 1 + r.nextInt();
		//shape among the sketch in x,y,z-Achse
		for (int y=0; y<rows-1; y++) {
			pg.beginShape();
			
			for(int x=0;x<cols;x++) {
				
				scape[x][y] = pg.parent.map(pg.parent.noise(Xoff,Yoff), 0, 1, -95, 100);
				Xoff += xoff;
				pg.translate(x,y);
				pg.rotate(flyA);
				pg.translate(-x,-y);
				
				
				pg.stroke( strokeColor);
				pg.rect(x*scale, y*scale, pg.width, pg.height);
				pg.stroke( strokeColor * scale);

				pg.rect(x*scale, (y+1)*scale, pg.width, pg.height);
				
			}
			Yoff += yoff;
			pg.endShape();
		}
		
	}
	


}
