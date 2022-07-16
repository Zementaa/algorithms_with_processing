package de.fernunihagen.mci.group2.coopalgoart.ccamier;

import processing.core.PApplet;
import processing.core.PImage;

public class Test extends PApplet {

	PImage pic;

	@Override
	public void settings() {
		size(500, 500);
	}

	@Override
	public void setup() {
		this.pic = loadImage("upstateny.jpg");

	}

	@Override
	public void draw() {
		// rect(mouseX, mouseY, 50, 50);

		// randomPoints();
		loadPic(this.pic);

	}

	@Override
	public void mousePressed() {
		randomBackground();
	}

	public void randomPoints() {
		float x = random(width);
		float y = random(height);
		fill(random(255), random(255), random(255), 25);
		ellipse(x, y, 15, 15);
	}

	public void randomBackground() {
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		background(r, g, b);
	}

	public void loadPic(PImage pic) {

		int x = (int) random(width);
		int y = (int) random(height);
		int c = pic.get(x, y);
		fill(c);
		ellipse(x, y, 10, 10);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Test mySketch = new Test();
		PApplet.runSketch(processingArgs, mySketch);
	}
}