package de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals;

import processing.core.PApplet;

/*
 * 
 */
public class Tree extends PApplet {
	float theta;

	@Override
	public void settings() {
		size(600, 400);

	}

	@Override
	public void setup() {
		smooth();
	}

	@Override
	public void draw() {
		background(255);
		// Let's pick an angle 0 to 90 degrees based on the mouse position
		this.theta = map(mouseX, 0, width, 0, PI / 2);

		// Start the tree from the bottom of the screen
		translate(width / 2, height);
		stroke(0);
		branch(100);

	}

	void branch(float len) {
		// Each branch will be 2/3rds the size of the previous one

		float sw = map(len, 2, 120, 1, 10);
		strokeWeight(sw);

		line(0, 0, 0, -len);
		// Move to the end of that line
		translate(0, -len);

		len *= 0.66;
		// All recursive functions must have an exit condition!!!!
		// Here, ours is when the length of the branch is 2 pixels or less
		if (len > 2) {
			pushMatrix(); // Save the current state of transformation (i.e. where are we now)
			rotate(this.theta); // Rotate by theta
			branch(len); // Ok, now call myself to draw two new branches!!
			popMatrix(); // Whenever we get back here, we "pop" in order to restore the previous matrix
							// state

			// Repeat the same thing, only branch off to the "left" this time!
			pushMatrix();
			rotate(-this.theta);
			branch(len);
			popMatrix();
		}
	}

	// reset board when mouse is pressed
	@Override
	public void mousePressed() {

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Tree mySketch = new Tree();
		PApplet.runSketch(processingArgs, mySketch);

	}

}
