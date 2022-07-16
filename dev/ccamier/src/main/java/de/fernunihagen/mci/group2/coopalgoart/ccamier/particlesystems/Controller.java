package de.fernunihagen.mci.group2.coopalgoart.ccamier.particlesystems;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/*
 * Alphabet
 * Axiom
 * Ruleset
 */
public class Controller extends PApplet {

	ParticleSystem ps;
	Random seed = new Random();

	@Override
	public void settings() {
		size(640, 360);

	}

	@Override
	public void setup() {
		PImage img = loadImage("texture.png");
		this.ps = new ParticleSystem(0, new PVector(width / 2, height - 75), img, new PGraphics(), seed, 6f, 0f);

	}

	@Override
	public void draw() {
		background(0);

		// Calculate a "wind" force based on mouse horizontal position
		float dx = map(mouseX, 0, width, -0.2f, 0.2f);
		PVector wind = new PVector(dx, 0);
		this.ps.vektorHinzufuegen(wind);
		this.ps.run();
		for (int i = 0; i < 2; i++) {
			this.ps.partikelHinzufuegen();
		}

		// Draw an arrow representing the wind force
		drawVector(wind, new PVector(width / 2, 50, 0), 500);

	}

	// Renders a vector object 'v' as an arrow and a position 'loc'
	void drawVector(PVector v, PVector pos, float scayl) {
		pushMatrix();
		float arrowsize = 4;
		// Translate to position to render vector
		translate(pos.x, pos.y);
		stroke(255);
		// Call vector heading function to get direction (note that pointing up is a
		// heading of 0) and rotate
		rotate(v.heading2D());
		// Calculate length of vector & scale it to be bigger or smaller if necessary
		float len = v.mag() * scayl;
		// Draw three lines to make an arrow (draw pointing up since we've rotate to the
		// proper direction)
		line(0, 0, len, 0);
		line(len, 0, len - arrowsize, +arrowsize / 2);
		line(len, 0, len - arrowsize, -arrowsize / 2);
		popMatrix();
	}

	@Override
	public void mousePressed() {

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Controller mySketch = new Controller();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
