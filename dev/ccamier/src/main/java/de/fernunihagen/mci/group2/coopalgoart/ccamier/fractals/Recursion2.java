package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

import processing.core.PGraphics;

public class Recursion2 {

	private int farbeKreise;

	// private int anzahlDurchgaenge;

	public Recursion2(int farbeKreise) {
		this.farbeKreise = farbeKreise;
		// this.anzahlDurchgaenge = anzahlDurchgaenge;
	}

	/*
	 * public int nextStep() { return this.anzahlDurchgaenge--; }
	 */

	public void render(int farbeKreise) {

		this.farbeKreise = farbeKreise;
		// this.anzahlDurchgaenge = anzahlDurchgaenge;

	}

	// Very simple function that draws one circle
	public void drawCircle(PGraphics pg, float x, float y, float radius) {
		pg.noFill();
		pg.stroke(farbeKreise);
		pg.ellipse(x, y, radius, radius);

	}
}
