package de.fernunihagen.mci.group2.coopalgoart.bwinzen.swarm;

import processing.core.PApplet;

/**
 * @author bwinzen
 *
 */
public class SwarmSimulation extends PApplet {
	private static final int WIDTH = 1600;
	private static final int HEIGHT = 800;

	private Swarm swarm = new Swarm(0, 10000, 4, false, WIDTH, HEIGHT);

	public void settings() {
		size(WIDTH, HEIGHT);
	}

	@Override
	public void setup() {

	}

	public void draw() {
		background(64);
		fill(255,0 ,0);
//		System.out.println(swarm.getBoidList().size());
		swarm.forEach(this::draw);
		swarm.nextStep(null);
	}

	void draw(Boid b) {
		ellipse((float)b.getX(), (float)b.getY(), 5, 5);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		SwarmSimulation mySketch = new SwarmSimulation();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
