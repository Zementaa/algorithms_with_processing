package de.fernunihagen.mci.group2.coopalgoart.ccamier.particlesystems;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class ParticleSystem {
	PGraphics pg;

	ArrayList<Particle> partikel; // Liste für alle Partikel
	PVector origin; // Hier entstehen Partikel
	PImage img;

	Random seed;

	float x;
	float y;

	public ParticleSystem(int num, PVector v, PImage img_, PGraphics app, Random rand, float x, float y) {
		this.pg = app;
		this.partikel = new ArrayList<Particle>(); // Initialisieren
		this.origin = v.get(); // Entstehungspunkt
		this.img = img_;
		this.seed = rand;
		this.x = x;
		this.y = y;
		for (int i = 0; i < num; i++) {
			this.partikel.add(new Particle(this.origin, this.img, this.pg, this.seed, this.x, this.y));
		}
	}

	public void run() {
		for (int i = this.partikel.size() - 1; i >= 0; i--) {
			Particle p = this.partikel.get(i);
			p.run();
			if (p.isDead()) {
				this.partikel.remove(i);
			}
		}
	}

	// Hier wird ein Vektor zu allen bereits bestehenden Partikeln hinzugefügt
	public void vektorHinzufuegen(PVector dir) {

		for (Particle p : this.partikel) {
			p.vektorHinzufuegen(dir);
		}
	}

	public void partikelHinzufuegen() {
		this.partikel.add(new Particle(this.origin, this.img, this.pg, this.seed, this.x, this.y));
	}
}
