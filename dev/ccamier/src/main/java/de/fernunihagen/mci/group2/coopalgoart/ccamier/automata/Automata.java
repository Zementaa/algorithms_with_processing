package de.fernunihagen.mci.group2.coopalgoart.ccamier.automata;

import processing.core.PApplet;

/*
 * Wolfram automata
 * 
 * Grid of cells [array like containing 0 and 1]
 * Cell: state [0 or 1], neighborhood [cells left and right]
 * Cell_state(t) = f(neighborhood_states(t-1))
 * 
 */
public class Automata extends PApplet {
	CA ca;

	@Override
	public void settings() {
		size(1800, 1400);

	}

	@Override
	public void setup() {
		background(255);

		// int[] ruleset = { 0, 1, 1, 1, 1, 0, 1, 1 }; // Rule 22
		int[] ruleset = { 0, 1, 1, 1, 1, 1, 0, 1 }; // Rule 190
		// int[] ruleset = {0,1,1,1,1,0,0,0}; // Rule 30
		// int[] ruleset = {0,1,1,1,0,1,1,0}; // Rule 110
		// int[] ruleset = {0,1,0,1,1,0,1,0}; // Rule 90

		this.ca = new CA(ruleset, this);

	}

	@Override
	public void draw() {
		this.ca.display(); // Draw the CA
		if (this.ca.generation < height / this.ca.w) {
			this.ca.generate();
		}
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Automata mySketch = new Automata();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
