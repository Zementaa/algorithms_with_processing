package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import processing.core.PApplet;

public class PerlinNoise extends PApplet {
	public static int background = 0;
	public static int strokeColor = 200;
	int cols, rows;
	int pixelPerLine = 3;
	int rasterWidth = 1800;
	int rasterHeight = 1500;
	float fly = 0;
	float[][] scape;
	double increase_fly = (0.5);
	double xOff = 0.15/(20/pixelPerLine);
	double yOff = 0.15/(20/pixelPerLine);

	public void settings() {
		size(1000, 1000, P3D);
	}

	public void setup() {
		// calc colums and rows
		cols = rasterWidth / pixelPerLine;
		rows = rasterHeight / pixelPerLine;
		scape = new float[cols][rows];
	}

	public void draw() {
		// increase the startponit y called fly
		fly -= increase_fly;
		float yoff = fly;
		for (int y = 0; y < rows; y++) {
			float xoff = 0;
			for (int x = 0; x < cols; x++) {
				scape[x][y] = map(noise(xoff, yoff), 0, 1, -95, 100);
				xoff += xOff;
			}
			yoff += yOff;
		}

		background(background);
		
		stroke(strokeColor,150);
		noFill();
//		noStroke();
//		fill(255, 255, 255);
//		pushMatrix();
//		translate(mouseX, mouseY, -10);
//		sphere(10);
//		popMatrix();
		
//		fill(0, 255, 0);
//		pointLight(255, 255, 255, mouseX, mouseY, -10);

		// rotate the pixels on the screen
		translate(width / 2, height / 2);
		rotateX(PI / 2.2f);
		translate(-rasterWidth / 2, -rasterHeight / 2);

		
		// shaping among the sketch in x,y,z-Achse
		for (int y = 0; y < rows - 1; y++) {
			beginShape();
			for (int x = 0; x < cols; x++) {
				vertex(x * pixelPerLine, y * pixelPerLine, scape[x][y]);
				vertex(x * pixelPerLine, (y + 1) * pixelPerLine, scape[x][y + 1]);

			}
			endShape();
		}
		
//		pushMatrix();
//		int col = cols/2;
//		int row = rows*5/8;
//		translate(col*pixelPerLine, row * pixelPerLine, scape[col][row]+10);
//		fill(0xFF, 0, 0);
//		noStroke();
//		sphere(pixelPerLine/2);
//		popMatrix();

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		PerlinNoise mySketch = new PerlinNoise();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
