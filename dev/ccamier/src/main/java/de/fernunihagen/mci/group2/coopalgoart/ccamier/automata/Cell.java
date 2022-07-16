package de.fernunihagen.mci.group2.coopalgoart.ccamier.automata;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Cell {

	PApplet pa = new PApplet();

	float x, y;
	float w;

	int farbe1, farbe2;

	int state;
	int vorherig;

	Cell(float x, float y, float w, int farbe1, int farbe2, Random rand) {
		this.x = x;
		this.y = y;
		this.w = w;

		this.farbe1 = farbe1;
		this.farbe2 = farbe2;

		this.state = rand.nextInt(2);

		this.vorherig = this.state;
	}

	void savePrevious() {
		this.vorherig = this.state;
	}

	void newState(int s) {
		this.state = s;
	}

	void display(PGraphics pg) {
		if (this.vorherig == 0 && this.state == 1)
			pg.fill(farbe1); // birth
		else if (this.state == 1)
			pg.fill(0); // black
		else if (this.vorherig == 1 && this.state == 0)
			pg.fill(farbe2); // death
		else
			pg.fill(255, 0); // transparent
		pg.stroke(0);
		pg.rect(this.x, this.y, this.w, this.w);
	}
}
