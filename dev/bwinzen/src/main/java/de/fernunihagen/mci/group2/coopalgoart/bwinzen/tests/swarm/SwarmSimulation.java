package de.fernunihagen.mci.group2.coopalgoart.bwinzen.tests.swarm;

import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class SwarmSimulation extends PApplet {
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	
	
	private Swarm swarm = new Swarm(0, 100, WIDTH, HEIGHT);


	public void settings() {
		size(WIDTH, HEIGHT);
	}

	@Override
	public void setup() {

	}

	public void draw() {
		background(64);
		swarm.forEach(this::draw);
		swarm.nextStep();
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	void draw(Bird b) {
		pushMatrix();
		translate((float) b.x, (float) b.y);

//		System.out.println(b.angle());
		rotate((float) b.angle());
		fill(0xFF, 0, 0);
		rect(-1, 0, 2, 10);
		fill(0, 0, 0);
		rect(-1, 0, 2, -10);
		popMatrix();
	}

	public void mousePressed() {
		background(64);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		SwarmSimulation mySketch = new SwarmSimulation();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
