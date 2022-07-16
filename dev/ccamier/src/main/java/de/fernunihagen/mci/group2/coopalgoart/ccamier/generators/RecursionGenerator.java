package de.fernunihagen.mci.group2.coopalgoart.ccamier.generators;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.fractals.Recursion2;
import lombok.Builder;
import processing.core.PGraphics;

@Builder
public class RecursionGenerator implements Generator {

	private int farbeKreise;
	private int x;
	private int y;
	private int radius;

	Recursion2 rec;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {

		if (this.rec == null) {
			this.rec = new Recursion2(farbeKreise);

			this.x = pg.width / 2;
			this.y = pg.height / 2;
			this.radius = pg.width / 2;
			this.rec.drawCircle(pg, x, y, radius);
		}

		/*
		 * anzahlDurchgaenge = rec.nextStep(); if (this.anzahlDurchgaenge > 0) {
		 * 
		 * }
		 */
		draw(pg);
	}

	void draw(PGraphics pg) {

		// System.out.println(anzahlDurchgaenge);

		this.rec.drawCircle(pg, x + radius / 2, y, radius);
		this.rec.drawCircle(pg, x - radius / 2, y, radius);
		this.rec.drawCircle(pg, x, y + radius / 2, radius);
		this.rec.drawCircle(pg, x, y - radius / 2, radius);

		radius = radius / 2;
		x = x + 10;
		y = y + 10;

	}

}
