package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests;

import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class BirdAnimation extends PApplet {

	private int state = 0;
	public void settings() {
		size(1000, 1000);
	}

//	public void draw() {
//		stroke(255);
//		noFill();
////		noStroke();
//		int controlY2 = state % 200;
//		boolean up=true;
//		int controlY = state%100;
//		if(controlY2 != controlY) {
//			controlY = 100 - controlY;
//			up=false;
//		}
//		beginShape();
//		 vertex(500-100, 500-controlY/2);
//		 quadraticVertex(500-50, 500-controlY, 500, 500);
//		 quadraticVertex(500+50, 500-controlY, 500+100, 500-controlY/2);
//		 endShape();
//		state++;
//	}
	int birdSize = 200;
	int birdWingSize = birdSize / 2;
	int birdWingMaxHeigh = birdSize / 4;

	public void draw() {
		stroke(255);
		background(0);
		noFill();
		pushMatrix();
		translate(width/2, height/2);

//			noStroke();
		int controlY2 = state % (birdWingMaxHeigh*2);
		int controlY = state % birdWingMaxHeigh;
		if (controlY2 != controlY) {
			controlY = birdWingMaxHeigh - controlY;
		}
		beginShape();
		vertex(-birdWingSize, -controlY / 2);
		quadraticVertex(-birdWingSize / 2 + controlY/2, -controlY, 0, 0);
		quadraticVertex(birdWingSize / 2 - controlY/2, -controlY, birdWingSize, -controlY / 2);
		endShape();
		state++;
		popMatrix();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		BirdAnimation mySketch = new BirdAnimation();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
