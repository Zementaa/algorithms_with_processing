package de.fernunihagen.mci.group2.coopalgoart.ccamier.generators;

import java.util.Random;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.particlesystems.ParticleSystem;
import lombok.Builder;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

@Builder
public class ParticleSystemGenerator implements Generator {

	ParticleSystem ps1;
	ParticleSystem ps2;
	ParticleSystem ps3;
	ParticleSystem ps4;
	String filePath1;
	String filePath2;
	String filePath3;
	String filePath4;

	PGraphics pg;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {

		this.pg = pg;

		if (this.ps1 == null) {

			PImage img1 = context.getPApplet().loadImage(filePath1);
			PImage img2 = context.getPApplet().loadImage(filePath2);
			PImage img3 = context.getPApplet().loadImage(filePath3);
			PImage img4 = context.getPApplet().loadImage(filePath4);

			Random rand = new Random(context.getSeed());
			this.ps1 = new ParticleSystem(0, new PVector(pg.width / 2, pg.height / 2), img1, pg, rand, 15.0f, 0.0f);
			this.ps2 = new ParticleSystem(0, new PVector(pg.width / 2, pg.height / 2), img2, pg, rand, 0.0f, -15.0f);
			this.ps3 = new ParticleSystem(0, new PVector(pg.width / 2, pg.height / 2), img3, pg, rand, -15.0f, 0.0f);
			this.ps4 = new ParticleSystem(0, new PVector(pg.width / 2, pg.height / 2), img4, pg, rand, 0.0f, 15.0f);
		}

		// float dx = PApplet.map(context.getPApplet().mouseX, 0, pg.width, -0.2f,
		// 0.2f);
		float dx = PApplet.map(pg.width / 2, 0, pg.width, -0.2f, 0.2f);
		PVector wind = new PVector(dx, 0);
		this.ps1.vektorHinzufuegen(wind);
		this.ps1.run();

		this.ps2.vektorHinzufuegen(wind);
		this.ps2.run();

		this.ps3.vektorHinzufuegen(wind);
		this.ps3.run();

		this.ps4.vektorHinzufuegen(wind);
		this.ps4.run();

		for (int i = 0; i < 2; i++) {
			this.ps1.partikelHinzufuegen();
			this.ps2.partikelHinzufuegen();
			this.ps3.partikelHinzufuegen();
			this.ps4.partikelHinzufuegen();
		}

		// Draw an arrow representing the wind force
		// drawVector(wind, new PVector(pg.width / 2, 50, 0), 500);

	}

	// Renders a vector object 'v' as an arrow and a position 'loc'
	void drawVector(PVector v, PVector pos, float scayl) {
		pg.pushMatrix();
		float arrowsize = 4;
		// Translate to position to render vector
		pg.translate(pos.x, pos.y);
		pg.stroke(255);
		// Call vector heading function to get direction (note that pointing up is a
		// heading of 0) and rotate
		pg.rotate(v.heading2D());
		// Calculate length of vector & scale it to be bigger or smaller if necessary
		float len = v.mag() * scayl;
		// Draw three lines to make an arrow (draw pointing up since we've rotate to the
		// proper direction)
		pg.line(0, 0, len, 0);
		pg.line(len, 0, len - arrowsize, +arrowsize / 2);
		pg.line(len, 0, len - arrowsize, -arrowsize / 2);
		pg.popMatrix();

	}

}
