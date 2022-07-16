package de.fernunihagen.mci.group2.coopalgoart.ccamier.particlesystems;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Particle {

	PGraphics pg;

	PVector pos;
	PVector vel;
	PVector acc;
	float lifespan;
	PImage img;

	Particle(PVector l, PImage img_, PGraphics pg, Random seed, float x, float y) {
		this.pg = pg;
		this.acc = new PVector(0, 0);
		double vx = seed.nextFloat() * x;
		double vy = seed.nextFloat() * y;
		this.vel = new PVector((float) vx, (float) vy);
		this.pos = l.get();
		this.lifespan = 100.0f;
		this.img = img_;
	}

	void run() {
		update();
		render();
	}

	// Method to apply a force vector to the Particle object
	// Note we are ignoring "mass" here
	void vektorHinzufuegen(PVector f) {
		this.acc.add(f);
	}

	// Method to update position
	void update() {
		this.vel.add(this.acc);
		this.pos.add(vel);
		this.lifespan -= 2.5f;
		this.acc.mult(0); // clear Acceleration
	}

	// Method to display
	void render() {
		this.pg.imageMode(PApplet.CENTER);
		this.pg.tint(255, this.lifespan);
		this.pg.image(this.img, this.pos.x, this.pos.y);
		// Drawing a circle instead
		// fill(255,lifespan);
		// noStroke();
		// ellipse(pos.x,pos.y,img.width,img.height);
	}

	// Is the particle still useful?
	boolean isDead() {
		if (this.lifespan < 0.0) {
			return true;
		} else {
			return false;
		}
	}
}
