package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

import processing.core.PApplet;

public class Recursion extends PApplet {

	@Override
	public void settings() {
		size(640, 360);

	}

	@Override
	public void setup() {

	}

	@Override
	public void draw() {
		background(255);
		drawCircle(width / 2, height / 2, 400);
		noLoop();

	}

	// Very simple function that draws one circle
	// and recursively calls itself
	void drawCircle(float x, float y, float radius) {
		noFill();
		stroke(0);
		ellipse(x, y, radius, radius);
		if (radius > 8) {
			// Four circles! left right, up and down
			drawCircle(x + radius / 2, y, radius / 2);
			drawCircle(x - radius / 2, y, radius / 2);
			drawCircle(x, y + radius / 2, radius / 2);
			// drawCircle(x, y - radius / 2, radius / 2);
		}
	}

	// reset board when mouse is pressed
	@Override
	public void mousePressed() {

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Recursion mySketch = new Recursion();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
