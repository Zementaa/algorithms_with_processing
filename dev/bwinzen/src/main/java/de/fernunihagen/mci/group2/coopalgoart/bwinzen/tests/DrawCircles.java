package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class DrawCircles extends PApplet {

	public void settings() {
		size(500, 500);
	}

	public void draw() {
		ellipse(mouseX, mouseY, 50, 50);
	}

	public void mousePressed() {
		background(64);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		DrawCircles mySketch = new DrawCircles();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
