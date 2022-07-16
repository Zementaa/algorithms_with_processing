package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * @author bwinzen
 *
 */
public class Magnet extends PApplet {

	private static final int POLE_SIZE = 10;
	private static final int WIDTH = 1800;
	private static final int HEIGHT = 1200;

	private int rasterWidth = 20;
	private int xDim = WIDTH / rasterWidth;
	private int yDim = HEIGHT / rasterWidth;
	private float[][][] centers = new float[xDim][yDim][2];
	private boolean reversed;
	
	public void settings() {
		size(WIDTH, HEIGHT);
	}

	@Override
	public void mouseClicked() {
		reversed = !reversed;
	}
	
	@Override
	public void setup() {
		for (int x = 0; x < width / rasterWidth; x++) {
			float xCent = rasterWidth/2 + rasterWidth * x;
			for (int y = 0; y < height / rasterWidth; y++) {
				float yCent = rasterWidth/2 + rasterWidth * y;
				centers[x][y] = new float[] { xCent, yCent };
			}
		}
		PFont f = createFont("Times New Roman", 12);
		textFont(f);
		textAlign(CENTER, CENTER);
	}

	public void draw() {
		background(64);
		fill(0);
		for (int x = 0; x < xDim; x++) {
			for (int y = 0; y < yDim; y++) {
				float[] center = centers[x][y];
				float vecX = center[0] - mouseX;
				float vecY = center[1] - mouseY;
				float angle = (float) Math.acos(vecY/dist(0, 0, vecX, vecY));
				if(mouseX<center[0]) {
					angle = (float) (2*Math.PI-angle);
				}
//				text(String.format("%2.2f",degrees(angle)), center[0], center[1]);
				pushMatrix();
				translate(center[0], center[1]);
				rotate(angle);
				if(reversed) {
					fill(0xFF, 0, 0);
				}else {
					fill(0, 0, 0xFF);
				}
				rect(-1, 0, 2, POLE_SIZE);
				if(reversed) {
					fill(0, 0, 0xFF);
				}else {
					fill(0xFF, 0, 0);
				}
				rect(-1, 0, 2, -POLE_SIZE);
				popMatrix();
			}
			
		}
	}

	public void mousePressed() {
		background(64);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Magnet mySketch = new Magnet();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
