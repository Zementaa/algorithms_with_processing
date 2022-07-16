package de.fernunihagen.mci.group2.coopalgoart.ccamier.automata;

import processing.core.PApplet;

//Wolfram Cellular Automata

class CA {

	PApplet app;

	int[] cells; // An array of 0s and 1s
	int generation; // How many generations?

	int[] ruleset; // An array to store the ruleset

	int w = 10;

	public CA(int[] ruleset, PApplet app) {
		this.app = app;
		this.ruleset = ruleset;
		this.cells = new int[this.app.width / this.w];
		for (int i = 0; i < this.cells.length; i++) {
			this.cells[i] = 0;
		}
		this.cells[this.cells.length / 2] = 1; // We arbitrarily start with just the middle cell having a state of "1"
		this.generation = 0;
	}

	// The process of creating the new generation
	void generate() {
		// First we create an empty array for the new values
		int[] nextgen = new int[this.cells.length];
		// For every spot, determine new state by examining current state, and neighbor
		// states
		// Ignore edges that only have one neighbor
		for (int i = 1; i < this.cells.length - 1; i++) {
			int left = this.cells[i - 1]; // Left neighbor state
			int me = this.cells[i]; // Current state
			int right = this.cells[i + 1]; // Right neighbor state
			nextgen[i] = rules(left, me, right); // Compute next generation state based on ruleset
		}
		// The current generation is the new generation
		this.cells = nextgen;
		this.generation++;

	}

	// This is the easy part, just draw the this.cells, fill 255 for '1', fill 0 for
	// '0'
	void display() {
		for (int i = 0; i < this.cells.length; i++) {
			if (this.cells[i] == 1)
				this.app.fill(0);
			else
				this.app.fill(255);
			this.app.noStroke();
			this.app.rect(i * this.w, this.generation * this.w, this.w, this.w);
		}
	}

	// Implementing the Wolfram rules
	// Could be improved and made more concise, but here we can explicitly see what
	// is going on for each case
	int rules(int a, int b, int c) {
		if (a == 1 && b == 1 && c == 1)
			return this.ruleset[0];
		if (a == 1 && b == 1 && c == 0)
			return this.ruleset[1];
		if (a == 1 && b == 0 && c == 1)
			return this.ruleset[2];
		if (a == 1 && b == 0 && c == 0)
			return this.ruleset[3];
		if (a == 0 && b == 1 && c == 1)
			return this.ruleset[4];
		if (a == 0 && b == 1 && c == 0)
			return this.ruleset[5];
		if (a == 0 && b == 0 && c == 1)
			return this.ruleset[6];
		if (a == 0 && b == 0 && c == 0)
			return this.ruleset[7];
		return 0;
	}
}
