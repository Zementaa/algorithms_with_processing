package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

import processing.core.PApplet;

public class GOL {

	PApplet app;

	int w = 8;
	int columns, rows;

	public GOL(PApplet app) {
		// Initialize rows, columns and set-up arrays
		this.app = app;
		this.columns = this.app.width / this.w;
		this.rows = this.app.height / this.w;

	}

}
