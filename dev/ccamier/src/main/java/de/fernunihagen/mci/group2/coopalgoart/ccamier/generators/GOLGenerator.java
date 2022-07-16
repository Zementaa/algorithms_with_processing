package de.fernunihagen.mci.group2.coopalgoart.ccamier.generators;

import de.fernunihagen.mci.group2.coopalgoart.api.CooperationContext;
import de.fernunihagen.mci.group2.coopalgoart.api.Generator;
import de.fernunihagen.mci.group2.coopalgoart.ccamier.automata.GOL;
import lombok.Builder;
import processing.core.PGraphics;

@Builder
public class GOLGenerator implements Generator {

	GOL gol;

	// Verhältnis Element zu Spielbrett
	int verhaeltnis;
	int farbe1;
	int farbe2;

	@Override
	public void nextStep(CooperationContext context, PGraphics pg) {

		if (this.gol == null) {
			this.gol = new GOL(pg, verhaeltnis, farbe1, farbe2, context);
		}

		this.gol.generate();
		this.gol.display(pg);

	}

}
