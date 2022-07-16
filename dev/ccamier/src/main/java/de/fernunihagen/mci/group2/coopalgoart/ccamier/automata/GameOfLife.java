package de.fernunihagen.mci.group2.coopalgoart.ccamier.automata;

import processing.core.PApplet;
import processing.core.PGraphics;

/*
 * Game of Life
 * 
 * Was ist mit den 9 Zellen um mich herum?
 * 
 * Tod: 
 * * Überbevölkerung: 4 oder mehr Zellen leben
 * * Einsamkeit: 1 oder keine Zelle leben
 * 
 * Geburt: Genau 3 lebende Nachbarn
 * 
 * Stasis: Alle anderen Szenarien: Nichts ändert sich
 * 
 */
public class GameOfLife extends PApplet {
	GOL gol;

	PGraphics pg = new PGraphics();

	@Override
	public void settings() {
		size(640, 360);

	}

	@Override
	public void setup() {
		// this.gol = new GOL(this.pg, 10, 110, 120, new CooperationContext());
	}

	@Override
	public void draw() {
		background(255);

		this.gol.generate();
		this.gol.display(this.pg);
	}

	// reset board when mouse is pressed
	@Override
	public void mousePressed() {
		// this.gol.init();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		GameOfLife mySketch = new GameOfLife();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
